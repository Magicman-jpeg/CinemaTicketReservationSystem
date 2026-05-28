const rows = "ABCDEFGHIJK".split("");
const cols = Array.from({ length: 10 }, (_, i) => i + 1);
const grid = document.getElementById("seat-grid");
const statusText = document.getElementById("status");
const reserveBtn = document.getElementById("reserveBtn");
let selectedSeat = null;
let seatData = [];

fetch("js/seat-data.json")
  .then((r) => r.json())
  .then((data) => {
    seatData = data;
    renderSeats();
  })
  .catch(() => {
    statusText.textContent = "Unable to load seat data.";
  });

function renderSeats() {
  grid.innerHTML = "";
  for (const row of rows) {
    for (const col of cols) {
      const seatNo = `${row}${col}`;
      const seat = document.createElement("div");
      seat.className = "seat";
      seat.textContent = seatNo;
      const item = seatData.find((s) => s.seatNo === seatNo);
      if (item && item.booked) seat.classList.add("booked");
      seat.addEventListener("click", () => onSeatClick(seatNo, seat));
      grid.appendChild(seat);
    }
  }
}

function onSeatClick(seatNo, seatEl) {
  if (seatEl.classList.contains("booked")) {
    statusText.textContent = "This seat is already booked.";
    return;
  }
  document.querySelectorAll(".seat.selected").forEach((s) => s.classList.remove("selected"));
  seatEl.classList.add("selected");
  selectedSeat = seatNo;
  statusText.textContent = `Selected ${seatNo}`;
}

reserveBtn.addEventListener("click", () => {
  if (!selectedSeat) {
    statusText.textContent = "Select a seat first.";
    return;
  }
  const seat = seatData.find((s) => s.seatNo === selectedSeat);
  if (seat && seat.booked) {
    statusText.textContent = "Double booking prevented.";
    return;
  }
  if (seat) seat.booked = true;
  renderSeats();
  statusText.textContent = `Seat ${selectedSeat} reserved in UI.`;
  selectedSeat = null;
});

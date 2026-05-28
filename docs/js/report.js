function fillTable(tableId, rows, mapper) {
  const tbody = document.querySelector(`#${tableId} tbody`);
  tbody.innerHTML = "";
  rows.forEach((row) => {
    const tr = document.createElement("tr");
    mapper(row).forEach((v) => {
      const td = document.createElement("td");
      td.textContent = v;
      tr.appendChild(td);
    });
    tbody.appendChild(tr);
  });
}

fetch("js/report-data.json")
  .then((r) => r.json())
  .then((data) => {
    fillTable("ticketsTable", data.ticketsPerMovie, (r) => [r.movie, r.tickets]);
    fillTable("dailyTable", data.dailyRevenue, (r) => [r.date, r.revenue]);
    fillTable("weeklyTable", data.weeklyRevenue, (r) => [r.week, r.revenue]);
    fillTable("discountTable", data.discountUsage, (r) => [r.discountType, r.count, r.totalDiscount]);
  });

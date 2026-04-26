import { h as head, e as escape_html, d as ensure_array_like, b as attr_class, i as stringify, f as attr_style } from "../../../../chunks/renderer.js";
function _page($$renderer, $$props) {
  $$renderer.component(($$renderer2) => {
    let contracts = [];
    head("2nyem4", $$renderer2, ($$renderer3) => {
      $$renderer3.title(($$renderer4) => {
        $$renderer4.push(`<title>Contracts — FMRZ Admin</title>`);
      });
    });
    $$renderer2.push(`<div class="container"><div class="page-header" style="display:flex; justify-content:space-between; align-items:center;"><div><h1>Service <span class="text-gradient">Contracts</span></h1> <p class="text-muted">Manage and provision phone lines across the subscriber base</p></div> <button class="btn btn-primary">${escape_html("Provision New Line")}</button></div> `);
    {
      $$renderer2.push("<!--[-1-->");
    }
    $$renderer2.push(`<!--]--> <div class="table-wrapper animate-fade"><table><thead><tr><th>ID</th><th>MSISDN</th><th>Customer</th><th>Plan</th><th>Status</th><th>Credit</th></tr></thead><tbody><!--[-->`);
    const each_array_2 = ensure_array_like(contracts);
    for (let $$index_2 = 0, $$length = each_array_2.length; $$index_2 < $$length; $$index_2++) {
      let c = each_array_2[$$index_2];
      $$renderer2.push(`<tr><td><span class="id-badge">#${escape_html(c.id)}</span></td><td><span class="phone-num">${escape_html(c.msisdn)}</span></td><td style="font-weight:600">${escape_html(c.customerName || "—")}</td><td><span class="badge badge-customer">${escape_html(c.rateplanName || "—")}</span></td><td><span${attr_class(`badge status-${stringify(c.status)}`, "svelte-2nyem4")}>${escape_html(c.status)}</span></td><td><span class="amount-num"${attr_style(c.availableCredit < 0 ? "color: #ef4444" : "")}>${escape_html(c.availableCredit)} EGP</span></td></tr>`);
    }
    $$renderer2.push(`<!--]--></tbody></table></div></div>`);
  });
}
export {
  _page as default
};

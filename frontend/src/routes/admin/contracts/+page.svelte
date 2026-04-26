<script>
  let contracts = $state([]);
  let customers = $state([]);
  let plans = $state([]);
  let loading = $state(false);
  let showForm = $state(false);

  // Form State
  let newMsisdn = $state('');
  let selectedCustomer = $state('');
  let selectedPlan = $state('');

  async function loadData() {
    try {
      const [cRes, uRes, pRes] = await Promise.all([
        fetch('/api/admin/contracts', { credentials: 'include' }),
        fetch('/api/admin/customers', { credentials: 'include' }),
        fetch('/api/admin/rateplans', { credentials: 'include' })
      ]);
      if (cRes.ok) contracts = await cRes.json();
      if (uRes.ok) customers = await uRes.json();
      if (pRes.ok) plans = await pRes.json();
    } catch {}
  }

  $effect(() => {
    loadData();
  });

  async function provisionLine(e) {
    e.preventDefault();
    loading = true;
    try {
      const res = await fetch('/api/admin/contracts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          msisdn: newMsisdn,
          userId: selectedCustomer,
          planId: selectedPlan
        })
      });
      if (res.ok) {
        showForm = false;
        newMsisdn = '';
        loadData();
      } else {
        const err = await res.json();
        alert(err.error || 'Provisioning failed');
      }
    } finally {
      loading = false;
    }
  }
</script>

<svelte:head><title>Contracts — FMRZ Admin</title></svelte:head>

<div class="container">
  <div class="page-header" style="display:flex; justify-content:space-between; align-items:center;">
    <div>
      <h1>Service <span class="text-gradient">Contracts</span></h1>
      <p class="text-muted">Manage and provision phone lines across the subscriber base</p>
    </div>
    <button class="btn btn-primary" onclick={() => showForm = !showForm}>
      {showForm ? 'Cancel' : 'Provision New Line'}
    </button>
  </div>

  {#if showForm}
    <div class="card animate-fade" style="margin-bottom: 2rem; max-width: 600px;">
      <h2 style="margin-bottom:1.5rem">Provision New Line</h2>
      <form onsubmit={provisionLine}>
        <div class="grid-2">
          <div class="form-group">
            <label class="label">MSISDN (Phone Number)</label>
            <input class="input" placeholder="e.g. 01012345678" bind:value={newMsisdn} required />
          </div>
          <div class="form-group">
            <label class="label">Select Customer</label>
            <select class="input" bind:value={selectedCustomer} required>
              <option value="">-- Select Customer --</option>
              {#each customers as u}
                <option value={u.id}>{u.name} ({u.msisdn})</option>
              {/each}
            </select>
          </div>
        </div>
        <div class="form-group">
          <label class="label">Select Rate Plan</label>
          <select class="input" bind:value={selectedPlan} required>
            <option value="">-- Select Plan --</option>
            {#each plans as p}
              <option value={p.id}>{p.name} - {p.basic_fee} EGP/mo</option>
            {/each}
          </select>
        </div>
        <button type="submit" class="btn btn-primary" style="width:100%" disabled={loading}>
          {loading ? 'Provisioning...' : 'Confirm Provisioning'}
        </button>
      </form>
    </div>
  {/if}

  <div class="table-wrapper animate-fade">
    <table>
      <thead>
        <tr><th>ID</th><th>MSISDN</th><th>Customer</th><th>Plan</th><th>Status</th><th>Credit</th></tr>
      </thead>
      <tbody>
        {#each contracts as c}
          <tr>
            <td><span class="id-badge">#{c.id}</span></td>
            <td><span class="phone-num">{c.msisdn}</span></td>
            <td style="font-weight:600">{c.customerName||'—'}</td>
            <td><span class="badge badge-customer">{c.rateplanName||'—'}</span></td>
            <td><span class="badge status-{c.status}">{c.status}</span></td>
            <td>
              <span class="amount-num" style={c.availableCredit < 0 ? 'color: #ef4444' : ''}>
                {c.availableCredit} EGP
              </span>
            </td>
          </tr>
        {/each}
      </tbody>
    </table>
  </div>
</div>

<style>
  .badge.status-active { background: rgba(34, 197, 94, 0.1); color: #22c55e; border: 1px solid rgba(34, 197, 94, 0.2); }
  .badge.status-suspended { background: rgba(224, 8, 0, 0.1); color: var(--red-light); border: 1px solid rgba(224, 8, 0, 0.2); }
</style>

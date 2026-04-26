<script>
  let contracts = $state([]);
  let customers = $state([]);
  let ratePlans = $state([]);
  let showModal = $state(false);
  let newContract = $state({ customerId: '', ratePlanId: '', msisdn: '', creditLimit: 100 });
  let error = $state('');
  let loading = $state(false);
  let success = $state('');

  async function loadData() {
    try {
      const [contractRes, custRes, planRes] = await Promise.all([
        fetch('/api/admin/contracts', { credentials: 'include' }),
        fetch('/api/admin/customers', { credentials: 'include' }),
        fetch('/api/admin/rateplans', { credentials: 'include' })
      ]);
      if (contractRes.ok) contracts = await contractRes.json();
      if (custRes.ok) customers = await custRes.json();
      if (planRes.ok) ratePlans = await planRes.json();
    } catch (e) {
      console.error('Failed to load data:', e);
    }
  }

  async function createContract(e) {
    e.preventDefault();
    error = '';
    success = '';
    loading = true;

    try {
      const res = await fetch('/api/admin/contracts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          userId: parseInt(newContract.customerId),
          ratePlanId: parseInt(newContract.ratePlanId),
          msisdn: newContract.msisdn,
          creditLimit: parseFloat(newContract.creditLimit)
        })
      });

      if (res.ok) {
        success = 'Contract created successfully!';
        showModal = false;
        newContract = { customerId: '', ratePlanId: '', msisdn: '', creditLimit: 100 };
        loadData();
      } else {
        const data = await res.json();
        error = data.error || 'Failed to create contract';
      }
    } catch (err) {
      error = 'Network error. Please try again.';
    } finally {
      loading = false;
    }
  }

  $effect(() => { loadData(); });
</script>
<svelte:head><title>Contracts — FMRZ Admin</title></svelte:head>
<div class="container">
  <div class="page-header">
    <h1>Service <span class="text-gradient">Contracts</span></h1>
    <p class="text-muted">Manage and provision phone lines across the subscriber base</p>
  </div>

  <div class="search-bar animate-fade">
    <div style="display:flex;gap:1rem">
      <button class="btn btn-primary" onclick={() => { showModal = true; error = ''; }}>+ Create Contract</button>
    </div>
  </div>

  {#if success}
    <div class="success-msg animate-fade">{success}</div>
  {/if}

  <div class="table-wrapper animate-fade"><table>
    <thead><tr><th>ID</th><th>MSISDN</th><th>Customer</th><th>Plan</th><th>Status</th><th>Credit</th></tr></thead>
    <tbody>{#each contracts as c}<tr>
      <td><span class="id-badge">#{c.id}</span></td>
      <td><span class="phone-num">{c.msisdn}</span></td>
      <td style="font-weight:600">{c.customer_name||'—'}</td>
      <td><span class="badge badge-customer">{c.rateplan_name||'—'}</span></td>
      <td><span class="badge badge-{c.status}">{c.status}</span></td>
      <td>
        <span class="amount-num" style={c.available_credit < 0 ? 'color: #ef4444' : ''}>
          {c.available_credit} EGP
        </span>
      </td>
    </tr>{/each}</tbody>
  </table></div>
</div>

{#if showModal}
<div
  class="modal-overlay"
  role="button"
  tabindex="0"
  aria-label="Close dialog"
  onclick={() => showModal = false}
  onkeydown={(e) => { if (e.key === 'Escape' || e.key === 'Enter' || e.key === ' ') showModal = false; }}
>
<div
  class="modal card-glass animate-fade"
  role="dialog"
  tabindex="-1"
  aria-modal="true"
  aria-label="Create new contract"
  onclick={e => e.stopPropagation()}
  onkeydown={(e) => { if (e.key === 'Escape') showModal = false; }}
>
  <h2>Create New Contract</h2>
  {#if error}
    <div class="error-msg animate-fade">{error}</div>
  {/if}
  <form onsubmit={createContract}>
    <div class="form-group">
      <label class="label" for="contract_customer">Customer</label>
      <select id="contract_customer" class="input" bind:value={newContract.customerId} required>
        <option value="">Select a customer...</option>
        {#each customers as cust}
          <option value={cust.id}>{cust.name} ({cust.username})</option>
        {/each}
      </select>
    </div>
    <div class="form-group">
      <label class="label" for="contract_rateplan">Rate Plan</label>
      <select id="contract_rateplan" class="input" bind:value={newContract.ratePlanId} required>
        <option value="">Select a rate plan...</option>
        {#each ratePlans as plan}
          <option value={plan.id}>{plan.name} - EGP {plan.price}/month</option>
        {/each}
      </select>
    </div>
    <div class="form-group">
      <label class="label" for="contract_msisdn">Phone Number (MSISDN)</label>
      <input id="contract_msisdn" class="input" type="text" bind:value={newContract.msisdn} placeholder="201000000000" required />
    </div>
    <div class="form-group">
      <label class="label" for="contract_credit">Credit Limit (EGP)</label>
      <input id="contract_credit" class="input" type="number" bind:value={newContract.creditLimit} placeholder="100" min="1" step="0.01" required />
    </div>
    <div style="display:flex;gap:1rem;justify-content:flex-end"><button type="button" class="btn btn-secondary" onclick={() => showModal = false}>Cancel</button><button type="submit" class="btn btn-primary" disabled={loading}>{loading ? 'Creating...' : 'Create Contract'}</button></div>
  </form>
</div></div>
{/if}

<style>
  .search-bar { margin-bottom: 2rem; }
  .modal-overlay{position:fixed;inset:0;background:rgba(0,0,0,.6);display:flex;align-items:center;justify-content:center;z-index:200;backdrop-filter:blur(4px)}
  .modal{width:100%;max-width:500px;padding:2rem}
  .modal h2{margin-bottom:1.5rem}
  .error-msg{background:rgba(239,68,68,0.1);border:1px solid rgba(239,68,68,0.2);color:#EF4444;padding:0.75rem;border-radius:var(--radius-sm);font-size:0.85rem;margin-bottom:1rem}
  .success-msg{background:rgba(34,197,94,0.1);border:1px solid rgba(34,197,94,0.2);color:#22C55E;padding:0.75rem;border-radius:var(--radius-sm);font-size:0.85rem;margin-bottom:1rem}
</style>


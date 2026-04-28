<script>
  let customers = $state([]);
  let search = $state('');
  let showModal = $state(false);
  let showSuccess = $state(false);
  let error = $state('');
  let newCustomer = $state({
    name: '',
    email: '',
    username: '',
    password: '',     // ← add this
    address: '',
    birthdate: ''
  });

  async function load() {
    const res = await fetch(`/api/admin/customers`);
    if (res.ok) customers = await res.json();
  }

  async function createCustomer(e) {
    e.preventDefault();
    error = '';
    const res = await fetch('/api/admin/customers', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newCustomer)
    });
    if (res.ok) {
      showModal = false;
      showSuccess = true;
      newCustomer = { name: '', email: '', username: '', password: '', address: '', birthdate: '' };
      load();
    } else {
      const data = await res.json();
      error = data.error || 'Failed to create customer';
    }
  }

  $effect(() => { load(); });
</script>

<svelte:head>
  <title>Customers — FMRZ</title>
</svelte:head>

<div class="container">
  <div class="page-header">
    <h1>Customer <span class="text-gradient">Directory</span></h1>
    <p class="text-muted">Manage subscriber profiles and account information</p>
  </div>

  <div class="search-bar animate-fade">
    <div style="display:flex;gap:1rem">
      <div style="position: relative;">
        <span style="position: absolute; left: 12px; top: 50%; transform: translateY(-50%); color: #64748b;">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
               fill="none" stroke="currentColor" stroke-width="2"
               stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/>
          </svg>
        </span>
        <input class="input" style="width:300px; padding-left: 2.5rem;"
               placeholder="Search directory..." bind:value={search}
               oninput={() => load()} aria-label="Search customers" />
      </div>
      <button class="btn btn-primary"
              style="display: flex; align-items: center; gap: 8px; padding: 0.75rem 1.5rem;"
              onclick={() => { showModal = true; error = ''; }}>
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
             fill="none" stroke="currentColor" stroke-width="2.5"
             stroke-linecap="round" stroke-linejoin="round">
          <path d="M5 12h14"/><path d="M12 5v14"/>
        </svg>
        Add New Customer
      </button>
    </div>
  </div>

  <div class="table-wrapper static-table animate-fade">
    <table>
      <thead>
      <tr>
        <th>ID</th><th>USERNAME</th><th>Name</th>
        <th>Email</th><th>Address</th><th>Birthdate</th>
      </tr>
      </thead>
      <tbody>
      {#each customers as c}
        <tr>
          <td><span class="id-badge">#{c.id}</span></td>
          <td><span class="phone-num" style="color: var(--red) !important;">{c.username}</span></td>
          <td class="customer-name" style="color: #FFFFFF !important;">{c.name}</td>
          <td style="color: #94A3B8 !important; font-size: 0.9rem;">{c.email || '—'}</td>
          <td style="color: #FB7185 !important; font-size: 0.9rem;">{c.address || '—'}</td>
          <td style="color: #64748B !important; font-size: 0.9rem;">{c.birthdate || '—'}</td>
        </tr>
      {:else}
        <tr><td colspan="6" style="text-align:center; color: var(--text-muted); padding: 2rem;">
          No customers found
        </td></tr>
      {/each}
      </tbody>
    </table>
  </div>
</div>

{#if showModal}
  <div class="modal-overlay"
       onclick={() => showModal = false}
       role="button" tabindex="0"
       onkeydown={(e) => e.key === 'Escape' && (showModal = false)}>
    <div class="modal card-glass animate-fade"
         onclick={e => e.stopPropagation()} role="dialog">
      <h2>Add New Customer</h2>
      {#if error}
        <div class="error-msg animate-fade">{error}</div>
      {/if}
      <form onsubmit={createCustomer}>
        <div class="form-group">
          <label class="label">Full Name</label>
          <input class="input" bind:value={newCustomer.name}
                 placeholder="Ahmed Ali" required />
        </div>
        <div class="form-group">
          <label class="label">Email Address</label>
          <input class="input" type="email" bind:value={newCustomer.email}
                 placeholder="ahmed@email.com" required />
        </div>
        <div class="form-group">
          <label class="label">Username</label>
          <input class="input" bind:value={newCustomer.username}
                 placeholder="john_doe" required />
        </div>
        <div class="form-group">
          <label class="label">Password</label>
          <input class="input" type="password" bind:value={newCustomer.password}
                 placeholder="Min 6 characters" required minlength="6" />
        </div>
        <div class="form-group">
          <label class="label">Mailing Address</label>
          <input class="input" bind:value={newCustomer.address}
                 placeholder="123 Street, City" />
        </div>
        <div class="form-group">
          <label class="label">Birth Date</label>
          <input class="input" type="date" bind:value={newCustomer.birthdate} />
        </div>
        <div style="display:flex;gap:1rem;justify-content:flex-end;margin-top:2rem">
          <button type="button" class="btn btn-secondary"
                  onclick={() => showModal = false}>Cancel</button>
          <button type="submit" class="btn btn-primary">Create Profile</button>
        </div>
      </form>
    </div>
  </div>
{/if}

{#if showSuccess}
  <div class="modal-overlay"
       onclick={() => showSuccess = false}
       role="button" tabindex="0"
       onkeydown={(e) => e.key === 'Escape' && (showSuccess = false)}>
    <div class="modal card-glass animate-fade"
         onclick={e => e.stopPropagation()} role="dialog">
      <div style="text-align:center;padding:1rem">
        <div style="margin-bottom:1.5rem">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24"
               fill="none" stroke="var(--red)" stroke-width="2"
               stroke-linecap="round" stroke-linejoin="round">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
            <polyline points="22 4 12 14.01 9 11.01"/>
          </svg>
        </div>
        <h2>Profile Created!</h2>
        <p class="text-muted" style="margin-bottom:2rem">
          Customer profile saved successfully.
        </p>
        <div style="display:flex;flex-direction:column;gap:1rem">
          <a href="/admin/contracts" class="btn btn-primary">
            Go to Contracts to Assign Line
          </a>
          <button class="btn btn-secondary"
                  onclick={() => showSuccess = false}>Stay on Directory</button>
        </div>
      </div>
    </div>
  </div>
{/if}

<style>
  .static-table {
    margin-top: 2rem;
    background: rgba(15, 15, 25, 0.6);
    border: 1px solid var(--border);
    border-radius: 20px;
    overflow: hidden;
    transition: none !important;
    transform: none !important;
    box-shadow: var(--shadow-premium);
  }
  .static-table:hover {
    background: rgba(15, 15, 25, 0.6) !important;
    transform: none !important;
    border-color: var(--border) !important;
    box-shadow: var(--shadow-premium) !important;
  }
  table { width: 100%; border-collapse: collapse; }
  th {
    text-align: left; padding: 1rem;
    background: rgba(255,255,255,0.05);
    color: #cbd5e1; font-weight: 600;
    border-bottom: 1px solid var(--border);
  }
  td { padding: 1rem; border-bottom: 1px solid var(--border); }
  tr:last-child td { border-bottom: none; }
  .id-badge {
    background: rgba(255,255,255,0.05);
    padding: 0.2rem 0.5rem;
    border-radius: 6px;
    font-size: 0.8rem;
    color: #94a3b8;
  }
  .phone-num { font-family: 'JetBrains Mono', monospace; font-weight: 600; }
  .customer-name { font-weight: 700; font-size: 1.05rem; }
  .modal-overlay {
    position: fixed; inset: 0;
    background: rgba(0,0,0,0.7);
    display: flex; align-items: center; justify-content: center;
    z-index: 200; backdrop-filter: blur(8px);
  }
  .modal { width: 100%; max-width: 480px; padding: 2.5rem; transform: none !important; }
  .error-msg {
    background: rgba(239,68,68,0.1);
    border: 1px solid rgba(239,68,68,0.2);
    color: #EF4444;
    padding: 0.75rem;
    border-radius: var(--radius-sm);
    font-size: 0.85rem;
    margin-bottom: 1rem;
  }
</style>
<script>
  let plans = $state([]);
  let servicePkgs = $state([]);
  let loading = $state(true);

  async function loadData() {
    try {
      const [plansRes, pkgsRes] = await Promise.all([
        fetch('/api/public/rateplans'),
        fetch('/api/public/service-packages')
      ]);
      if (plansRes.ok) plans = await plansRes.json();
      if (pkgsRes.ok) servicePkgs = await pkgsRes.json();
    } catch (e) {
      plans = [];
      servicePkgs = [];
    }
    loading = false;
  }

  function handleMouseMove(e, id) {
    const card = document.getElementById(id);
    if (!card) return;
    const rect = card.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    card.style.setProperty('--mouse-x', `${x}px`);
    card.style.setProperty('--mouse-y', `${y}px`);
  }

  $effect(() => {
    loadData();
  });
</script>

<svelte:head>
  <title>Packages — FMRZ</title>
</svelte:head>

<div class="container">
  <div class="page-header">
    <div>
      <h1>Rate Plans & <span class="text-gradient">Packages</span></h1>
      <p class="page-subtitle">Premium communication solutions tailored for the digital age</p>
    </div>
  </div>

  {#if loading}
    <div class="loading">Loading...</div>
  {:else}
    <!-- ─── BENTO RATE PLANS ─── -->
    <h2 class="section-title"><span class="text-gradient">Standard</span> Rate Plans</h2>

    <div class="bento-grid">
      {#each plans as plan, i}
        <div 
          id="plan-card-{i}"
          class="bento-card card animate-fade" 
          class:featured={i === 1}
          style="animation-delay: {i * 0.1}s; --glow-color: {plan.name.includes('Basic') ? 'rgba(0, 150, 255, 0.25)' : plan.name.includes('Elite') ? 'rgba(255, 165, 0, 0.25)' : 'rgba(224, 8, 0, 0.2)'};"
          onmousemove={(e) => handleMouseMove(e, `plan-card-${i}`)}
        >
          <div class="glow-layer"></div>
          
          <div class="badge-container">
            {#if i === 1}
              <div class="plan-badge popular">⭐ Most Popular</div>
            {:else}
              <div class="badge-spacer"></div>
            {/if}
          </div>

          <div class="plan-header">
            <h3>{plan.name}</h3>
            <div class="plan-price">
              <span class="currency">EGP</span>
              <span class="amount">{plan.price}</span>
              <span class="period">/mo</span>
            </div>
          </div>

          <div class="plan-details">
            <div class="detail-row">
              <span class="detail-label">📞 Voice Rate</span>
              <span class="detail-value">{plan.ror_voice} <small>EGP/min</small></span>
            </div>
            <div class="detail-row">
              <span class="detail-label">🌐 Data Rate</span>
              <span class="detail-value">{plan.ror_data} <small>EGP/MB</small></span>
            </div>
            <div class="detail-row">
              <span class="detail-label">💬 SMS Rate</span>
              <span class="detail-value">{plan.ror_sms} <small>EGP/msg</small></span>
            </div>
            <div class="detail-row">
              <span class="detail-label">💳 Monthly Fee</span>
              <span class="detail-value">EGP {plan.price}</span>
            </div>
          </div>

          <button
            onclick={() => window.location.href = '/register?plan=' + plan.id}
            class="btn {i === 1 ? 'btn-primary' : 'btn-secondary'}"
            style="width: 100%; position: relative; z-index: 2;"
          >
            Choose {plan.name}
          </button>
        </div>
      {/each}
    </div>

    <!-- ─── SERVICE PACKAGES ─── -->
    {#if servicePkgs.length > 0}
      <h2 class="section-title" style="margin-top: 8rem;"><span class="text-gradient">Bundled</span> Service Packages</h2>
      <div class="bundles-bento">
        {#each servicePkgs as pkg, i}
          <div 
            id="pkg-card-{i}"
            class="bundle-card card animate-fade" 
            class:wide={i === 1 || i === 4}
            style="animation-delay: {i * 0.1}s; --glow-color: rgba(224, 8, 0, 0.1);"
            onmousemove={(e) => handleMouseMove(e, `pkg-card-${i}`)}
          >
            <div class="glow-layer"></div>

            <div class="badge-container">
              {#if pkg.is_roaming}
                <div class="plan-badge roaming">🌍 Roaming Ready</div>
              {:else if pkg.price === 0 || pkg.price === null}
                <div class="plan-badge promo">🎁 Exclusive Deal</div>
              {:else if i === 0}
                <div class="plan-badge trend">🔥 Trending</div>
              {:else}
                <div class="badge-spacer"></div>
              {/if}
            </div>

            <div class="plan-header">
              <h3>{pkg.name}</h3>
              <p class="pkg-subtitle">{pkg.description ?? ''}</p>
              {#if pkg.price !== null}
                <div class="plan-price">
                  <span class="currency">EGP</span>
                  <span class="amount" style="font-size: 2.2rem;">{pkg.price}</span>
                  <span class="period">per month</span>
                </div>
              {/if}
            </div>

            <div class="plan-features">
              {#if pkg.type === 'voice'}
                <div class="feature-row">
                  <span class="feature-label">📞 Voice</span>
                  <span class="feature-value">{pkg.amount} Min</span>
                </div>
              {:else if pkg.type === 'data'}
                <div class="feature-row">
                  <span class="feature-label">🌐 Data</span>
                  <span class="feature-value">{pkg.amount} MB</span>
                </div>
              {:else if pkg.type === 'sms'}
                <div class="feature-row">
                  <span class="feature-label">💬 SMS</span>
                  <span class="feature-value">{pkg.amount} Msg</span>
                </div>
              {:else if pkg.type === 'free_units'}
                <div class="feature-row">
                  <span class="feature-label">🎁 Free Units</span>
                  <span class="feature-value">{pkg.amount} Units</span>
                </div>
              {/if}
              <div class="feature-row">
                <span class="feature-label">Priority</span>
                <span class="feature-value">{pkg.priority === 1 ? '⚡ High' : '📦 Standard'}</span>
              </div>
            </div>

            <button 
              onclick={() => window.location.href = '/register?pkg=' + pkg.id} 
              class="btn btn-secondary" 
              style="width: 100%; margin-top: 1.5rem; position: relative; z-index: 2;"
            >
              Choose Package
            </button>
          </div>
        {/each}
      </div>
    {/if}
  {/if}
</div>

<style>
  .bento-grid {
    display: grid;
    grid-template-columns: repeat(12, 1fr);
    gap: 1.5rem;
    max-width: 1200px;
    margin: 0 auto;
  }
  
  .bento-card {
    grid-column: span 3;
    padding: 2.2rem;
    display: flex;
    flex-direction: column;
    position: relative;
    overflow: hidden;
    background: rgba(15, 15, 25, 0.6);
    backdrop-filter: blur(20px);
    -webkit-backdrop-filter: blur(20px);
    border-radius: 24px;
    border: 1px solid rgba(255, 255, 255, 0.08);
    box-shadow: 
      inset 0 1px 1px rgba(255, 255, 255, 0.1),
      0 10px 30px rgba(0, 0, 0, 0.4);
    transition: all 0.4s cubic-bezier(0.16, 1, 0.3, 1);
  }

  .bento-card.featured {
    grid-column: span 6;
    background: rgba(20, 20, 30, 0.7);
    border-color: rgba(224, 8, 0, 0.25);
  }

  .bento-card:hover {
    transform: translateY(-8px);
    border-color: rgba(255, 255, 255, 0.15);
    box-shadow: 
      inset 0 1px 2px rgba(255, 255, 255, 0.2),
      0 20px 40px rgba(0, 0, 0, 0.6);
  }

  /* ── Aurora Glow ── */
  .glow-layer {
    position: absolute;
    inset: 0;
    pointer-events: none;
    background: radial-gradient(
      600px circle at var(--mouse-x, -1000px) var(--mouse-y, -1000px),
      var(--glow-color),
      transparent 80%
    );
    opacity: 0;
    transition: opacity 0.5s;
    z-index: 1;
  }
  .bento-card:hover .glow-layer, .bundle-card:hover .glow-layer { opacity: 1; }

  .plan-header { text-align: center; margin-bottom: 2rem; position: relative; z-index: 2; }
  .plan-header h3 {
    font-size: 1.8rem; font-weight: 900; color: white; margin-bottom: 0.75rem;
    letter-spacing: -0.05em; -webkit-font-smoothing: antialiased;
  }
  
  .featured .plan-header h3 {
    background: linear-gradient(to right, #fff 20%, var(--red-light) 40%, var(--red-light) 60%, #fff 80%);
    background-size: 200% auto; background-clip: text; -webkit-background-clip: text;
    -webkit-text-fill-color: transparent; animation: shine 3s linear infinite;
  }

  @keyframes shine { to { background-position: 200% center; } }
  
  .plan-price { display: flex; align-items: baseline; justify-content: center; gap: 0.25rem; }
  .amount {
    font-size: 3.5rem; font-weight: 900;
    background: linear-gradient(135deg, var(--red), var(--red-light));
    background-clip: text; -webkit-background-clip: text; -webkit-text-fill-color: transparent;
  }

  .plan-details, .plan-features {
    display: flex; flex-direction: column; gap: 1rem; padding: 1.5rem 0;
    border-top: 1px solid rgba(255,255,255,0.08);
    border-bottom: 1px solid rgba(255,255,255,0.08);
    flex: 1; margin-bottom: 2rem; position: relative; z-index: 2;
  }
  .detail-row, .feature-row { display: flex; justify-content: space-between; font-size: 0.95rem; }
  .detail-value, .feature-value { color: white; font-weight: 700; }

  .badge-container { display: flex; justify-content: center; align-items: center; height: 36px; margin-bottom: 0.75rem; position: relative; z-index: 2; }
  .plan-badge {
    display: inline-flex; align-items: center; justify-content: center;
    padding: 5px 16px; border-radius: 50px; font-size: 0.75rem; font-weight: 900;
    text-transform: uppercase; color: white; line-height: 1; gap: 8px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.3);
  }
  .popular { background: var(--red); }
  .roaming { background: #3b82f6; }
  .promo { background: #f59e0b; }
  .trend { background: #ef4444; }

  /* ── Bundles Bento ── */
  .bundles-bento {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 1.5rem;
    max-width: 1200px;
    margin: 0 auto;
  }
  .bundle-card {
    grid-column: span 1;
    padding: 2.2rem;
    border-radius: 24px;
    background: rgba(15, 15, 25, 0.6);
    backdrop-filter: blur(20px);
    -webkit-backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.08);
    box-shadow: inset 0 1px 1px rgba(255, 255, 255, 0.1);
    min-height: 480px;
    position: relative;
    overflow: hidden;
    transition: all 0.4s cubic-bezier(0.16, 1, 0.3, 1);
  }
  .bundle-card.wide { grid-column: span 2; }
  .bundle-card:hover { transform: translateY(-8px); border-color: rgba(255, 255, 255, 0.15); }

  .section-title {
    text-align: center;
    font-size: 3rem;
    font-weight: 900;
    margin-bottom: 4rem;
    color: white;
    letter-spacing: -0.05em;
  }

  @media (max-width: 1024px) {
    .bento-card, .bento-card.featured, .bundle-card, .bundle-card.wide { grid-column: span 12; transform: none !important; }
  }
</style>
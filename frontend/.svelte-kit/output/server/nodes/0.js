

export const index = 0;
let component_cache;
export const component = async () => component_cache ??= (await import('../entries/pages/_layout.svelte.js')).default;
export const universal = {
  "prerender": false,
  "ssr": false,
  "trailingSlash": "always"
};
export const universal_id = "src/routes/+layout.js";
export const imports = ["_app/immutable/nodes/0.YRrhHQ1-.js","_app/immutable/chunks/BN5kW0wy.js","_app/immutable/chunks/D7EsS0Nc.js","_app/immutable/chunks/BuO8yb-I.js","_app/immutable/chunks/BdcKkpBQ.js","_app/immutable/chunks/CHe8mBld.js","_app/immutable/chunks/D5ntfUca.js","_app/immutable/chunks/ByZyC6Y5.js","_app/immutable/chunks/CFEFqFVi.js","_app/immutable/chunks/CYXXdfle.js","_app/immutable/chunks/COWxLTr6.js"];
export const stylesheets = ["_app/immutable/assets/0.C5BfeEy7.css"];
export const fonts = [];



export const index = 0;
let component_cache;
export const component = async () => component_cache ??= (await import('../entries/pages/_layout.svelte.js')).default;
export const universal = {
  "prerender": false,
  "ssr": false,
  "trailingSlash": "always"
};
export const universal_id = "src/routes/+layout.js";
export const imports = ["_app/immutable/nodes/0.o_VS3AXp.js","_app/immutable/chunks/Cnri5IQm.js","_app/immutable/chunks/alBkrSSP.js","_app/immutable/chunks/BhmXjptP.js","_app/immutable/chunks/BX6sDNQ4.js","_app/immutable/chunks/DzQ3cZIB.js","_app/immutable/chunks/CRW4d5i-.js","_app/immutable/chunks/CYVjqzzc.js","_app/immutable/chunks/BKuqSeVd.js","_app/immutable/chunks/BJzq5ia5.js","_app/immutable/chunks/F5tfD2ne.js","_app/immutable/chunks/CVxNEjHX.js"];
export const stylesheets = ["_app/immutable/assets/0.PAgkI_Xf.css"];
export const fonts = [];

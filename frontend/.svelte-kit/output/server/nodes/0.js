

export const index = 0;
let component_cache;
export const component = async () => component_cache ??= (await import('../entries/pages/_layout.svelte.js')).default;
export const universal = {
  "prerender": false,
  "ssr": false,
  "trailingSlash": "always"
};
export const universal_id = "src/routes/+layout.js";
export const imports = ["_app/immutable/nodes/0.C1fIkhbh.js","_app/immutable/chunks/CZUpI4LC.js","_app/immutable/chunks/DmLBgqC5.js","_app/immutable/chunks/UyndxvoH.js","_app/immutable/chunks/TWgbmREG.js","_app/immutable/chunks/CmDGT2dF.js","_app/immutable/chunks/B1NbkbhI.js","_app/immutable/chunks/CRTnjvkf.js","_app/immutable/chunks/BEbCK4jy.js","_app/immutable/chunks/COhLwiK5.js","_app/immutable/chunks/CjRcZ3rL.js"];
export const stylesheets = ["_app/immutable/assets/0.ByyRAiBv.css"];
export const fonts = [];



export const index = 0;
let component_cache;
export const component = async () => component_cache ??= (await import('../entries/pages/_layout.svelte.js')).default;
export const universal = {
  "prerender": false,
  "ssr": false,
  "trailingSlash": "always"
};
export const universal_id = "src/routes/+layout.js";
export const imports = ["_app/immutable/nodes/0.CEE_EqfD.js","_app/immutable/chunks/C5ujjk74.js","_app/immutable/chunks/87bIvZSd.js","_app/immutable/chunks/D46fgPKJ.js","_app/immutable/chunks/DJBmNonr.js","_app/immutable/chunks/RAoeSuPI.js","_app/immutable/chunks/DIheLVo_.js","_app/immutable/chunks/DRJs6kCy.js","_app/immutable/chunks/6by0GS1d.js","_app/immutable/chunks/Cj6688aa.js","_app/immutable/chunks/CVvJ1TDU.js"];
export const stylesheets = ["_app/immutable/assets/0.DVXKzpj0.css"];
export const fonts = [];

import adapter from '@sveltejs/adapter-vercel';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	kit: {
		adapter: adapter({
			runtime: 'nodejs20.x'  // ← explicitly set Node version
		}),
		paths: {
			base: ""
		}
	}
};

export default config;
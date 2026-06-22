const API_BASE_URL = 'http://localhost:8080';
const SESSION_KEY = 'bdii.auth.session';

export function getSession() {
  try {
    const raw = localStorage.getItem(SESSION_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function setSession(data) {
  localStorage.setItem(SESSION_KEY, JSON.stringify(data));
}

export function clearSession() {
  localStorage.removeItem(SESSION_KEY);
}

export function authHeaders() {
  const session = getSession();
  if (!session?.token) return {};
  return { Authorization: `Bearer ${session.token}` };
}

export async function fetchJson(url, options = {}) {
  const { body, method = 'GET', params } = options;

  let fullUrl = `${API_BASE_URL}${url}`;
  if (params) {
    const qs = new URLSearchParams(
      Object.fromEntries(Object.entries(params).filter(([, v]) => v != null))
    ).toString();
    if (qs) fullUrl += `?${qs}`;
  }

  const response = await fetch(fullUrl, {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...authHeaders(),
      ...options.headers,
    },
    body: body ? JSON.stringify(body) : undefined,
  });

  if (!response.ok) {
    const text = await response.text().catch(() => '');
    throw new Error(text || `Error ${response.status}`);
  }

  const text = await response.text();
  if (!text) return null;
  return JSON.parse(text);
}

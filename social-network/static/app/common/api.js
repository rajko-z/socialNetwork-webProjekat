var api = axios.create({
    baseURL: 'http://localhost:8081/api/',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use(request => {
    const isLoggedIn = getCurrentUser() !== null;
    if (isLoggedIn) {
        request.headers.common.Authorization = `Bearer ${getToken()}`;
    }
    return request;
});

window.API = api;


import axios from 'axios';
import type {CsrfToken} from "@/assets/vo/CsrfToken";

let axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_SERVER_URL,
    withCredentials: true,
    xsrfCookieName: import.meta.env.VITE_CSRF_COOKIE_NAME,
    xsrfHeaderName: import.meta.env.VITE_CSRF_HEADER_NAME
});

let csrfToken: CsrfToken;
let isFetchingCsrfToken = false;

function getCsrfToken(): Promise<CsrfToken> {
    if (csrfToken != null) {
        return Promise.resolve(csrfToken);
    }

    if (isFetchingCsrfToken) {
        return new Promise(resolve => {
            const intervalId = setInterval(() => {
                if (csrfToken != null) {
                    clearInterval(intervalId);
                    resolve(csrfToken);
                }
            }, 100);
        });
    }

    isFetchingCsrfToken = true;

    return requestCsrfToken();
}

function requestCsrfToken() {
    return axiosInstance.get<CsrfToken>(import.meta.env.VITE_CSRF_TOKEN_URL)
        .then(response => {
            csrfToken = response.data;
            isFetchingCsrfToken = false;
            return csrfToken;
        });
}

setInterval(requestCsrfToken, 1700 * 1000);

axiosInstance.interceptors.request.use((config) => {
    if (config.method?.toLowerCase() === 'get') {
        return config;
    }
    return getCsrfToken().then(csrfToken => {
        config.headers.set(import.meta.env.VITE_CSRF_HEADER_NAME, csrfToken.token);
        return config;
    });
});

export default axiosInstance;
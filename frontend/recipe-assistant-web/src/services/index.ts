import axios from "axios";

export const http = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
    timeout: 20000
})

http.interceptors.response.use(
    (r) => r,
    (error) => {
        console.error('API Error', error?.response?.data || error.message)
        return Promise.reject(error);
    }
)
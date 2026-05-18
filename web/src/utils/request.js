import axios from 'axios';

/* axios功能封装  */

const service = axios.create({
    timeout: 5000,
    baseURL: 'http://localhost:8888',
    withCredentials: true
});

// 在全局API拦截器中
axios.interceptors.response.use(
    response => response,
    error => {
        console.error('API error:', error);
        if (error.response) {
            // 处理服务器返回的错误
            return Promise.reject(error.response.data);
        } else {
            // 处理网络错误
            return Promise.reject({ message: '网络连接失败' });
        }
    }
);
// request.js 中的请求拦截器
service.interceptors.request.use(
    config => {
        config.headers['Content-Type'] = 'application/json';
        // 强制清理 params，避免 ?null
        if (!config.params) config.params = undefined;
        // 新增：打印完整请求参数（包括 method、url、data）
        console.log(`[Request Detail] ${config.method.toUpperCase()} ${config.url}`);
        if (config.data) {
            console.log('[Request Data]', config.data);
        }
        return config;
    },
    error => {
        console.error('[Request Error]', error);
        return Promise.reject(error);
    }
);

service.interceptors.request.use(config => {
    console.log('最终请求配置:', config); // 检查 data 和 params
    return config;
});

// 响应拦截器
service.interceptors.response.use(
    response => {
        // 打印响应信息，便于调试
        console.log(`[Response] ${response.config.method.toUpperCase()} ${response.config.url} - ${response.status}`);
        console.log('[Response Data]', response.data);

        // 检查 HTTP 状态码
        if (response.status !== 200) {
            return Promise.reject(new Error(`HTTP error, status = ${response.status}`));
        }

        // 获取响应数据
        const res = response.data;

        // 检查业务状态码（根据你的后端约定）
        if (res.status_code !== 1) {
            // 业务错误，显示错误信息
            console.error('[Business Error]', res.message || '请求失败');
            return Promise.reject(new Error(res.message || '请求失败'));
        }

        // 成功响应
        return res;
    },
    error => {
        // 处理网络错误
        console.error('[Network Error]', error.message);

        // 获取错误信息
        let message = '网络错误，请稍后重试';
        if (error.response) {
            // 服务器返回了错误响应
            message = `服务器错误: ${error.response.status}`;
        } else if (error.request) {
            // 请求已发送，但没有收到响应
            message = '请求超时，请检查网络连接';
        }

        // 显示错误信息
        return Promise.reject(new Error(message));
    }
);

export default service;
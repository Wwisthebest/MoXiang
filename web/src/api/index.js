import request from '../utils/request';

const api = {

    // 用户功能 对应usercontroller
    phoneLogin(data) {
        return request({
            url: '/user/phone-login',
            method: 'post',
            data: {
                account: data.phone,  // 关键修改：字段名对齐后端
                password: data.password
            }
        });
    },
    emailLogin(data) {
        console.log('即将发送的邮箱登录数据:', data);
        return request({
            url: '/user/email-login', // 改为新的邮箱登录接口路径
            method: 'post',
            data: data
        });
    },
    logout(query) {
        return request({
            url: '/user/logout',
            method: 'post',
            data: {},
            withCredentials: true,
            timeout: 5000
        });
    },
    phoneRegister(data) {
        return request({
            url: '/user/phone-register',
            method: 'post',
            data: data
        });
    },
    emailRegister(data) {
        return request({
            url: '/user/email-register',
            method: 'post',
            data: data
        });
    },
    getEmailVerificationCode(data) {
        return request({
            url: '/user/email-code',
            method: 'post',
            data: data
        });
    },
    getUserInfo(query) {
        return request({
            url: '/user/info',
            method: 'get',
            params: query
        });
    },
    updateUserPublicInfo(data) {
        return request({
            url: '/user/info',
            method: 'post',
            data: data
        });
    },
    updatePassword(data) {  // 修改：根据后端调整为 POST
        return request({
            url: '/user/password',
            method: 'post',
            data: data
        });
    },


    //  地址功能  对应addresscontroller
    addAddress(data) {
        return request({
            url: '/address/add',
            method: 'post',
            data: data
        });
    },
    getAddress(query) {
        return request({
            url: '/address/info',
            method: 'get',
            params: query
        });
    },
    updateAddress(data) {
        return request({
            url: '/address/update',
            method: 'post',
            data: data
        });
    },
    deleteAddress(data) {
        return request({
            url: '/address/delete',
            method: 'post',
            data: data
        });
    },

    // 商品操作   对应idleitemController
    addIdleItem(data) {
        return request({
            url: '/idle/add',
            method: 'post',
            data: data
        });
    },
    getIdleItem(query) {
        return request({
            url: '/idle/info',
            method: 'get',
            params: query
        });
    },
    getAllIdleItem(query) {
        return request({
            url: '/idle/all',
            method: 'get',
            params: query
        });
    },
    findIdleTiem(query) {
        return request({
            url: '/idle/find',
            method: 'get',
            params: query
        });
    },
    findIdleTiemByLable(query) {
        return request({
            url: '/idle/lable',
            method: 'get',
            params: query
        });
    },
    updateIdleItem(data) {
        return request({
            url: '/idle/update',
            method: 'post',
            data: data
        });
    },

    // 个人中心的功能  ordercontroller
    addOrder(data) {
        return request({
            url: '/order/add',
            method: 'post',
            data: data
        });
    },
    getOrder(query) {
        return request({
            url: '/order/info',
            method: 'get',
            params: query
        });
    },
    updateOrder(data) {
        return request({
            url: '/order/update',
            method: 'post',
            data: data
        });
    },
    getMyOrder(query) {
        return request({
            url: '/order/my',
            method: 'get',
            params: query
        });
    },
    getMySoldIdle(query) {
        return request({
            url: '/order/my-sold',
            method: 'get',
            params: query
        });
    },

    // 订单的地址信息   orderAddressController
    addOrderAddress(data) {
        return request({
            url: '/order-address/add',
            method: 'post',
            data: data
        });
    },
    updateOrderAddress(data) {
        return request({
            url: '/order-address/update',
            method: 'post',
            data: data
        });
    },
    getOrderAddress(query) {
        return request({
            url: '/order-address/info',
            method: 'get',
            params: query
        });
    },

    // 收藏功能就是购物车    favoriteController
    addFavorite(data) {
        return request({
            url: '/favorite/add',
            method: 'post',
            data: data
        });
    },
    getMyFavorite(query) {
        return request({
            url: '/favorite/my',
            method: 'get',
            params: query
        });
    },
    deleteFavorite(query) {
        return request({
            url: '/favorite/delete',
            method: 'get',
            params: query
        });
    },
    checkFavorite(query) {
        return request({
            url: '/favorite/check',
            method: 'get',
            params: query
        });
    },

    // 留言功能   messagecontroller
    sendMessage(data) {
        return request({
            url: '/message/send',
            method: 'post',
            data: data
        });
    },
    getMessage(query) {
        return request({
            url: '/message/info',
            method: 'get',
            params: query
        });
    },
    getAllIdleMessage(query) {
        return request({
            url: '/message/idle',
            method: 'get',
            params: query
        });
    },
    getAllMyMessage(query) {
        return request({
            url: '/message/my',
            method: 'get',
            params: query
        });
    },
    deleteMessage(query) {
        return request({
            url: '/message/delete',
            method: 'get',
            params: query
        });
    },

    // 管理员相关操作
    getGoods(query) {
        return request({
            url: '/admin/idleList',
            method: 'get',
            params: query
        });
    },
    updateGoods(query) {
        return request({
            url: '/admin/updateIdleStatus',
            method: 'get',
            params: query
        });
    },
    adminLogin(data) {  // 修改：使用 POST 方法
        return request({
            url: '/admin/login',
            method: 'post',
            data: data
        });
    },
    loginOut(query) {
        return request({
            url: '/admin/loginOut',
            method: 'post',
            params: query
        });
    },
    queryIdle(query) {
        return request({
            url: '/admin/queryIdle',
            method: 'get',
            params: query
        });
    },
    queryOrder(query) {
        return request({
            url: '/admin/queryOrder',
            method: 'get',
            params: query
        });
    },
    queryUser(query) {
        return request({
            url: '/admin/queryUser',
            method: 'get',
            params: query
        });
    },
    // 用户管理相关API
    getUserData(data) {
        return request({
            url: '/admin/userList',
            method: 'get',
            params: {
                page: data.page,
                nums: data.nums,
                status: data.status || 0
            }
        });
    },

    getBadUserData(data) {
        return request({
            url: '/admin/userList',
            method: 'get',
            params: {
                page: data.page,
                nums: data.nums,
                status: 1
            }
        });
    },

    getUserManage(data) {
        return request({
            url: '/admin/list',
            method: 'get',
            params: {
                page: data.page,
                nums: data.nums
            }
        });
    },

    updateUserStatus(data) {
        return request({
            url: '/admin/updateUserStatus',
            method: 'post',
            params: {
                id: data.id,
                status: data.status
            }
        });
    },
    regAdministrator(data) {
        return request({
            url: '/admin/add',
            method: 'post',
            data: {  // 确保这里包含所有需要传递的字段
                adminName: data.adminName,
                accountNumber: data.accountNumber,
                adminPassword: data.adminPassword,
                phone: data.phone || null,       // 添加电话字段，若无则传null
                email: data.email || null,       // 添加邮箱字段
                sex: data.sex || null,          // 添加性别字段
                birth: data.birth || null       // 添加生日字段
            },
            headers: {
                'Content-Type': 'application/json'  // 明确指定内容类型
            }
        });
    },

    updateAdmin(data) {
        return request({
            url: '/admin/updateAdmin',
            method: 'post',
            data: data
        });
    },
    updateAlipay(query) {
        return request({
            url: '/alipay/pay',
            method: 'get',
            params: query
        });
    },
    getDetail(id) {
        return request({
            url: '/admin/getDetail',
            method: 'get',
            params: id
        });
    },
};


export default api;

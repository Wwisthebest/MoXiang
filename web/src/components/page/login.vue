<template>
    <div class="login-container">
        <el-card class="box-card">
            <div class="login-body">
                <div class="login-title" @click="toIndex">墨香循环——二手书交易平台</div>

                <!-- 登录类型选择 -->
                <div class="login-type-tabs">
                    <span :class="{ 'active-tab': loginType === 'phone' }" @click="loginType = 'phone'">
                        手机登录
                    </span>
                    <span :class="{ 'active-tab': loginType === 'email' }" @click="loginType = 'email'">
                        邮箱登录
                    </span>
                </div>

                <el-form ref="form" :model="userForm">
                    <!-- 手机登录表单 -->
                    <div v-if="loginType === 'phone'">
                        <el-input
                            placeholder="请输入手机号..."
                            v-model="userForm.account"
                            class="login-input"
                            :maxlength="loginType === 'phone' ? 11 : 50"
                        >
                            <template slot="prepend">
                                <div class="el-icon-phone"></div>
                            </template>
                        </el-input>
                    </div>

                    <!-- 邮箱登录表单 -->
                    <div v-else>
                        <el-input
                            placeholder="请输入邮箱..."
                            v-model="userForm.account"
                            class="login-input"
                            :maxlength="loginType === 'phone' ? 11 : 50"
                        >
                            <template slot="prepend">
                                <div class="el-icon-message"></div>
                            </template>
                        </el-input>
                    </div>

                    <el-input
                        placeholder="请输入密码..."
                        v-model="userForm.password"
                        class="login-input"
                        @keyup.enter.native="login"
                        show-password
                    >
                        <template slot="prepend">
                            <div class="el-icon-lock"></div>
                        </template>
                    </el-input>

                    <div class="login-submit">
                        <el-button type="primary" @click="login">登录</el-button>
                    </div>

                    <div class="other-submit">
                        <router-link to="/sign-in" class="sign-in-text">注册</router-link>
                        <router-link to="/login-admin" class="sign-in-text">管理员登录</router-link>
                    </div>
                </el-form>
            </div>
        </el-card>
    </div>
</template>

<script>
export default {
    name: "login",
    data() {
        return {
            loginType: "phone", // 默认显示手机登录
            userForm: {
                account: '',  // 动态存储手机号或邮箱
                password: ''
            }
        };
    },

    methods: {
        login() {
            // 基本校验
            if (!this.userForm.account || !this.userForm.password) {
                this.$message.error("请填写完整的登录信息！");
                return;
            }

            // 根据登录类型调用不同的API
            if (this.loginType === 'phone') {
                // 手机号格式校验
                const phoneRegex = /^1[3-9]\d{9}$/;
                if (!phoneRegex.test(this.userForm.account)) {
                    this.$message.error("请输入有效的手机号");
                    return;
                }

                // 调用手机登录API
                this.$api.phoneLogin({
                    phone: this.userForm.account,
                    password: this.userForm.password
                }).then(res => {
                    this.handleLoginResult(res);
                });
            } else {
                // 邮箱格式校验
                const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                if (!emailRegex.test(this.userForm.account)) {
                    this.$message.error("请输入有效的邮箱地址");
                    return;
                }

                // 调用邮箱登录API
                this.$api.emailLogin({
                    email: this.userForm.account,
                    password: this.userForm.password
                }).then(res => {
                    this.handleLoginResult(res);
                });
            }
        },

        handleLoginResult(res) {
            console.log(res);
            if (res.status_code === 1) {
                // 处理登录成功逻辑
                res.data.signInTime = res.data.signInTime.substring(0, 10);
                this.$globalData.userInfo = res.data;
                this.$router.replace({ path: '/index' });
            } else {
                this.$message.error(res.msg || "登录失败，请检查账号密码");
            }
        },

        toIndex() {
            this.$router.replace({ path: '/index' });
        }
    }
}
</script>

<style scoped>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    width: 100%;
    background-color: #f1f1f1;
}

.login-body {
    padding: 30px;
    width: 400px;
    height: 100%;
}

.login-title {
    padding-bottom: 20px;
    text-align: center;
    font-weight: 600;
    font-size: 20px;
    color: #409EFF;
    cursor: pointer;
}

.login-input {
    margin-bottom: 20px;
}

.login-submit {
    display: flex;
    justify-content: center;
}

.sign-in-container {
    padding: 0 10px;
}

.sign-in-text {
    color: #409EFF;
    font-size: 16px;
    text-decoration: none;
    line-height: 28px;
}

.other-submit {
    display: flex;
    justify-content: space-between;
    margin-top: 10px;
}

/* 新增的登录类型选项卡样式 */
.login-type-tabs {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #ebeef5;
}

.login-type-tabs span {
    padding: 0 20px;
    font-size: 16px;
    cursor: pointer;
    color: #606266;
}

.login-type-tabs span.active-tab {
    color: #409EFF;
    font-weight: 500;
}
</style>
<template>
    <div class="sign-in-container">
        <el-card class="box-card">
            <div class="sign-in-body">
                <!-- 注册类型选择 -->
                <div class="register-type-tabs">
          <span :class="{ 'active-tab': registerType === 'phone' }" @click="registerType = 'phone'">
            手机注册
          </span>
                    <span :class="{ 'active-tab': registerType === 'email' }" @click="registerType = 'email'">
            邮箱注册
          </span>
                </div>

                <!-- 手机注册表单 -->
                <div v-if="registerType === 'phone'">
                    <div class="sign-in-title">手机注册</div>
                    <el-input
                        placeholder="请输入昵称..."
                        maxlength="30"
                        v-model="userInfo.nickname"
                        class="sign-in-input"
                        clearable>
                        <template slot="prepend">
                            <div class="el-icon-user-solid"></div>
                        </template>
                    </el-input>
                    <el-input
                        placeholder="请输入手机号..."
                        maxlength="11"
                        v-model="userInfo.phone"
                        class="sign-in-input"
                        clearable>
                        <template slot="prepend">
                            <div class="el-icon-phone"></div>
                        </template>
                    </el-input>
                    <el-input
                        placeholder="请输入密码..."
                        show-password
                        maxlength="16"
                        v-model="userInfo.userPassword"
                        class="sign-in-input"
                        clearable>
                        <template slot="prepend">
                            <div class="el-icon-lock"></div>
                        </template>
                    </el-input>
                    <el-input
                        placeholder="请再次输入密码..."
                        show-password
                        maxlength="16"
                        v-model="userPassword2"
                        @keyup.enter.native="signIn"
                        class="sign-in-input"
                        clearable>
                        <template slot="prepend">
                            <div class="el-icon-lock"></div>
                        </template>
                    </el-input>
                </div>

                <!-- 邮箱注册表单 -->
                <div v-else>
                    <div class="sign-in-title">邮箱注册</div>
                    <el-input
                        placeholder="请输入昵称..."
                        maxlength="30"
                        v-model="userInfo.nickname"
                        class="sign-in-input"
                        clearable>
                        <template slot="prepend">
                            <div class="el-icon-user-solid"></div>
                        </template>
                    </el-input>
                    <el-input
                        placeholder="请输入邮箱..."
                        maxlength="50"
                        v-model="userInfo.email"
                        class="sign-in-input"
                        clearable>
                        <template slot="prepend">
                            <div class="el-icon-message"></div>
                        </template>
                    </el-input>
                    <!-- 修正验证码输入框的模板语法 -->
                    <el-input
                        placeholder="请输入验证码..."
                        maxlength="6"
                        v-model="verificationCode"
                        class="sign-in-input"
                        clearable>
                        <template #append> <!-- 使用新插槽语法 -->
                            <el-button
                                :disabled="countdown > 0"
                                @click="getEmailCode"
                                size="small">
                                {{ countdown > 0 ? `${countdown}s后重试` : '获取验证码' }}
                            </el-button>
                        </template>
                    </el-input>
                    <el-input
                        placeholder="请输入密码..."
                        show-password
                        maxlength="16"
                        v-model="userInfo.userPassword"
                        class="sign-in-input"
                        clearable>
                        <template slot="prepend">
                            <div class="el-icon-lock"></div>
                        </template>
                    </el-input>
                    <el-input
                        placeholder="请再次输入密码..."
                        show-password
                        maxlength="16"
                        v-model="userPassword2"
                        @keyup.enter.native="signIn"
                        class="sign-in-input"
                        clearable>
                        <template slot="prepend">
                            <div class="el-icon-lock"></div>
                        </template>
                    </el-input>
                </div>

                <div class="sign-in-submit">
                    <el-button type="primary" @click="signIn">提交</el-button>
                </div>
                <div class="login-container">
                    <span @click="toLogin" class="login-text">登录</span>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
export default {
    name: "sign-in",
    data() {
        return {
            registerType: "phone", // 默认显示手机注册
            userPassword2: "",
            verificationCode: "", // 邮箱注册使用
            countdown: 0, // 邮箱验证码倒计时
            userInfo: {
                phone: "",
                email: "", // 新增邮箱字段，确保userInfo包含email
                userPassword: "",
                nickname: ""
            }
        };
    },
    methods: {
        toLogin() {
            this.$router.replace({ path: "/login" });
        },

        getEmailCode() {
            if (!this.userInfo.email) {
                this.$message.error("请输入邮箱");
                return;
            }
            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(this.userInfo.email)) {
                this.$message.error("请输入有效的邮箱地址");
                return;
            }
            this.$api.getEmailVerificationCode({
                email: this.userInfo.email
            }).then(res => {
                if (res.status_code === 1) {
                    this.$message.success("验证码已发送，请注意查收");
                    this.startCountdown();
                } else {
                    this.$message.error(res.message || "获取验证码失败");
                }
            }).catch(e => {
                console.log(e);
                this.$message.error("网络异常，请稍后重试");
            });
        },

        startCountdown() {
            this.countdown = 60;
            const timer = setInterval(() => {
                this.countdown--;
                if (this.countdown <= 0) clearInterval(timer);
            }, 1000);
        },

        signIn() {
            // 统一校验必填字段
            if (this.registerType === "phone") {
                // 手机注册校验
                if (!this.userInfo.phone || !this.userInfo.nickname || !this.userInfo.userPassword || !this.userPassword2) {
                    this.$message.error("手机注册信息未填写完整！");
                    return;
                }
                if (this.userInfo.userPassword !== this.userPassword2) {
                    this.$message.error("两次输入的密码不相同！");
                    return;
                }
                const phoneRegex = /^1[3-9]\d{9}$/;
                if (!phoneRegex.test(this.userInfo.phone)) {
                    this.$message.error("请输入有效的手机号");
                    return;
                }
                // 发送手机注册请求
                this.$api.phoneRegister({
                    ...this.userInfo,
                    registerType: "phone" // 建议传递注册类型
                }).then(res => {
                    // 处理注册结果
                });
            } else {
                // 邮箱注册校验
                if (!this.userInfo.email || !this.userInfo.nickname || !this.userInfo.userPassword || !this.userPassword2 || !this.verificationCode) {
                    this.$message.error("邮箱注册信息未填写完整！");
                    return;
                }
                if (this.userInfo.userPassword !== this.userPassword2) {
                    this.$message.error("两次输入的密码不相同！");
                    return;
                }
                const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                if (!emailRegex.test(this.userInfo.email)) {
                    this.$message.error("请输入有效的邮箱地址");
                    return;
                }
                if (this.verificationCode.length !== 6) {
                    this.$message.error("请输入6位有效的验证码");
                    return;
                }
                // 发送邮箱注册请求（确保包含所有必填参数）
                this.$api.emailRegister({
                    ...this.userInfo,
                    verificationCode: this.verificationCode,
                    registerType: "email"
                }).then(res => {
                    // 处理注册结果
                });
            }
        }
    }
};
</script>

<script>
export default {
    name: "sign-in",
    data() {
        return {
            registerType: "phone", // 默认显示手机注册
            userPassword2: "",
            verificationCode: "", // 邮箱注册使用
            countdown: 0, // 邮箱验证码倒计时
            userInfo: {
                phone: "",
                userPassword: "",
                nickname: ""
            }
        };
    },
    methods: {
        toLogin() {
            this.$router.replace({ path: "/login" });
        },
        // 移除手机验证码相关方法（getPhoneCode 不再需要）

        // 保留邮箱验证码方法
        getEmailCode() {
            if (!this.userInfo.email) {
                this.$message.error("请输入邮箱");
                return;
            }
            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(this.userInfo.email)) {
                this.$message.error("请输入有效的邮箱地址");
                return;
            }
            this.$api.getEmailVerificationCode({
                email: this.userInfo.email
            }).then(res => {
                if (res.status_code === 1) {
                    this.$message.success("验证码已发送，请注意查收");
                    this.startCountdown();
                } else {
                    this.$message.error(res.message || "获取验证码失败");
                }
            }).catch(e => {
                console.log(e);
                this.$message.error("网络异常，请稍后重试");
            });
        },

        startCountdown() {
            this.countdown = 60;
            const timer = setInterval(() => {
                this.countdown--;
                if (this.countdown <= 0) clearInterval(timer);
            }, 1000);
        },

        signIn() {
            // 验证必填字段（手机注册无需验证码）
            // 确保 userInfo.phone 存在
            if (this.registerType === "phone") {
                // 手机注册：校验手机号
                if (!this.userInfo.phone) {
                    this.$message.error("请输入手机号");
                    return;
                }
            }
            if (this.registerType === "phone") {
                if (!this.userInfo.nickname || !this.userInfo.phone || !this.userInfo.userPassword || !this.userPassword2) {
                    this.$message.error("注册信息未填写完整！");
                    return;
                }
                if (this.userInfo.userPassword !== this.userPassword2) {
                    this.$message.error("两次输入的密码不相同！");
                    return;
                }
                const phoneRegex = /^1[3-9]\d{9}$/;
                if (!phoneRegex.test(this.userInfo.phone)) {
                    this.$message.error("请输入有效的手机号");
                    return;
                }
                // 发送手机注册请求（无需携带 verificationCode）
                this.$api.phoneRegister({
                    ...this.userInfo,
                    // 移除 verificationCode 参数
                }).then(res => {
                    if (res.status_code === 1) {
                        this.$message.success("注册成功！");
                        this.$router.replace({ path: "/login" });
                    } else {
                        this.$message.error(res.message || "注册失败");
                    }
                }).catch(e => {
                    console.log(e);
                    this.$message.error("注册失败，网络异常！");
                });
            } else {
                // 邮箱注册：确保 userInfo.email 存在
                if (!this.userInfo.email) {
                    this.$message.error("请输入邮箱");
                    return;
                }
                // 邮箱注册逻辑不变（需验证验证码）
                if (!this.userInfo.nickname || !this.userInfo.email || !this.userInfo.userPassword || !this.userPassword2 || !this.verificationCode) {
                    this.$message.error("注册信息未填写完整！");
                    return;
                }
                if (this.userInfo.userPassword !== this.userPassword2) {
                    this.$message.error("两次输入的密码不相同！");
                    return;
                }
                const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                if (!emailRegex.test(this.userInfo.email)) {
                    this.$message.error("请输入有效的邮箱地址");
                    return;
                }
                if (this.verificationCode.length !== 6) {
                    this.$message.error("请输入有效的验证码");
                    return;
                }
                this.$api.emailRegister({
                    ...this.userInfo,
                    verificationCode: this.verificationCode,
                    registerType: "email"
                }).then(res => {
                    if (res.status_code === 1) {
                        this.$message.success("注册成功！");
                        this.$router.replace({ path: "/login" });
                    } else {
                        this.$message.error(res.message || "注册失败，邮箱已被注册！");
                    }
                }).catch(e => {
                    console.log(e);
                    this.$message.error("注册失败，网络异常！");
                });
            }
        }
    }
};
</script>

<style scoped>
.sign-in-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    width: 100%;
    background-color: #f1f1f1;
}

.sign-in-body {
    padding: 30px;
    width: 400px;
    height: 100%;
}

.sign-in-title {
    padding-bottom: 30px;
    text-align: center;
    font-weight: 600;
    font-size: 20px;
    color: #409EFF;
}

.sign-in-input {
    margin-bottom: 20px;
}

.sign-in-submit{
    display: flex;
    justify-content: center;
}

.login-container{
    padding: 0 10px;
}

.login-text{
    color: #409EFF;
    font-size: 16px;
    cursor:pointer;
}

/* 新增的选项卡样式 */
.register-type-tabs {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #ebeef5;
}

.register-type-tabs span {
    padding: 0 20px;
    font-size: 16px;
    cursor: pointer;
    color: #606266;
}

.register-type-tabs span.active-tab {
    color: #409EFF;
    font-weight: 500;
}
</style>
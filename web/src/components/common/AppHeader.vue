<template>
    <div class="header">
        <div class="header-container">
            <div class="app-name">
                <router-link to="/">墨香循环——二手书交易平台</router-link>
            </div>
            <div class="search-container">
                <!-- 按回车触发函数  @keyup.enter.native -->
                <el-input placeholder="搜闲置..." v-model="searchValue" @keyup.enter.native="searchIdle">
                    <el-button slot="append" icon="el-icon-search" @click="searchIdle"></el-button>
                </el-input>
            </div>
            <el-button type="primary" icon="el-icon-plus"  @click="toRelease">发布闲置/公告</el-button>
            <el-button type="primary" icon="el-icon-chat-dot-round" @click="toMessage">消息</el-button>
            <router-link v-if="!isLogin" class="user-name-text" to="/login">登录</router-link>
            <el-dropdown trigger="click" v-else>
                <div style="cursor:pointer;display: flex;align-items: center;">
                    <div style="font-size: 16px;color: #409EFF;padding-right: 5px;">{{nicknameValue?nicknameValue:nickname}}</div>
                    <el-avatar :src="avatarValue?avatarValue:avatar"></el-avatar>
                </div>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item><div @click="toMe">个人中心</div></el-dropdown-item>
                    <el-dropdown-item divided style="color: red;"><div @click="loginOut">退出登录</div></el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>
<script>

    export default {
        name: 'Header',
        props: ['searchInput','nicknameValue','avatarValue'],
        data() {
            return {
                searchValue: this.searchInput,
                nickname:'登录',
                avatar:'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
                isLogin:false
            };
        },
        created(){
            // console.log("header");
            if(! this.$globalData.userInfo.nickname){
                this.$api.getUserInfo().then(res=>{
                    console.log('Header getUserInfo:',res);
                    if(res.status_code===1){
                        this.nickname=res.data.nickname;
                        this.avatar=res.data.avatar;
                        res.data.signInTime=res.data.signInTime.substring(0,10);
                        this.$globalData.userInfo=res.data;
                        this.isLogin=true;
                    }
                })
            }else {
                this.nickname=this.$globalData.userInfo.nickname;
                this.avatar=this.$globalData.userInfo.avatar;
                this.isLogin=true;
            }
        },
        methods: {
            searchIdle() {
                if ('/search' !== this.$route.path) {
                    this.$router.push({path: '/search', query: {searchValue: this.searchValue}});
                } else {
                    this.$router.replace({path: '/search', query: {searchValue: this.searchValue}});
                    this.$router.go(0);
                }

            },
            toMe() {
                if ('/me' !== this.$route.path) {
                    this.$router.push({path: '/me'});
                }
            },
            toMessage(){
                if ('/message' !== this.$route.path) {
                    this.$router.push({path: '/message'});
                }
            },
            toRelease(){
                if ('/release' !== this.$route.path) {
                    this.$router.push({path: '/release'});
                }
            },
            loginOut() {
                this.$api.logout()
                    .then(res => {
                        console.log('logout response:', res);

                        // 1. 重置全局状态
                        this.$globalData.userInfo = {};

                        // 2. 清除本地存储
                        localStorage.removeItem('token');
                        localStorage.removeItem('userInfo');
                        sessionStorage.clear();

                        // 3. 显示成功消息
                        this.$message.success('退出登录成功');

                        // 4. 导航到首页并刷新
                        this.$router.push('/index').then(() => {
                            // 确保导航完成后再刷新
                            setTimeout(() => {
                                window.location.reload();
                            }, 300);
                        });
                    })
                    .catch(error => {
                        console.error('退出登录错误:', error);

                        // 检查错误类型
                        if (error.response) {
                            // 服务器返回了错误响应
                            console.error('服务器错误:', error.response.data);
                            const errorMsg = error.response.data.message || '退出登录失败，请重试';
                            this.$message.error(errorMsg);
                        } else if (error.request) {
                            // 请求已发送，但没有收到响应
                            console.error('网络错误: 没有收到响应');
                            this.$message.error('网络连接失败，请检查网络设置');
                        } else {
                            // 其他错误
                            console.error('请求错误:', error.message);
                            this.$message.error('请求处理失败: ' + error.message);
                        }
                    });
            }



        }
    };
</script>
<style scoped>
    .header {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        width: 100%;
        height: 58px;
        background: #ffffff;
        display: flex;
        justify-content: center;
        border-bottom: #eeeeee solid 2px;
        z-index: 1000;
    }

    .header-container {
        width: 1000px;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .app-name a {
        color: #409EFF;
        font-size: 24px;
        text-decoration: none;
    }

    .search-container {
        width: 300px;
    }
    .user-name-text{
        font-size: 16px;
        font-weight: 600;
        color: #409EFF;
        cursor: pointer;
        text-decoration: none;
    }
</style>

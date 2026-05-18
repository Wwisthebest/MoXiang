<template>
    <div class="main-border">
        <el-menu default-active="1" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <span v-show="this.mode != 3" class="app-title">
            <el-input placeholder="搜索用户..." v-model="searchValue" @keyup.enter.native="searchIdle">
              <el-button slot="append" icon="el-icon-search" @click="searchIdle"></el-button>
            </el-input>
          </span>
            <el-menu-item index="1">正常用户</el-menu-item>
            <el-menu-item index="2">违规用户</el-menu-item>
            <el-menu-item index="3">管理员</el-menu-item>
            <div v-show="this.mode ==3" class="addAdminButton">
                <el-button size="mini" type="success" @click="adminRegVisible = true">添加管理员</el-button>
                <el-dialog
                    title="添加管理员"
                    :visible.sync="adminRegVisible"
                    width="30%"
                >
                    <span style="margin-left: 10px">管理员账号</span>
                    <el-input v-model="adminAccountNumber" minlength="5" maxlength="20"
                              placeholder="请输入管理员账号" style="padding: 10px 0"
                              clearable required></el-input>
                    <span style="margin-left: 10px">管理员名称</span>
                    <el-input v-model="adminName"  maxlength="8" placeholder="请输入管理员名称" style="padding: 10px 0" clearable required></el-input>
                    <span style="margin-left: 10px">手机号</span>
                    <el-input v-model="adminPhone" minlength="11" maxlength="11" placeholder="请输入11位手机号" style="padding: 10px 0"
                              clearable required></el-input>
                    <span style="margin-left: 10px">邮箱</span>
                    <el-input v-model="adminEmail" placeholder="请输入邮箱地址" style="padding: 10px 0"
                              clearable required></el-input>
                    <span style="margin-left: 10px">性别</span>
                    <el-select v-model="adminSex" placeholder="请选择性别" style="width: 80%; margin-left: 10px; padding: 10px 0" required>
                        <el-option label="男" value="男"></el-option>
                        <el-option label="女" value="女"></el-option>
                    </el-select><br>
                    <span style="margin-left: 10px">出生日期</span>
                    <el-date-picker
                        v-model="adminBirth"
                        type="date"
                        placeholder="选择出生日期"
                        style="width: 80%; margin-left: 10px; padding: 10px 0"
                        required>
                    </el-date-picker><br>
                    <span style="margin-left: 10px">管理员密码</span>
                    <el-input v-model="adminPassword" minlength="8" placeholder="请输入管理员密码" style="padding: 10px 0" show-password required></el-input>
                    <span style="margin-left: 10px">确认管理员密码</span>
                    <el-input v-model="adminRePassword" minlength="8" placeholder="请再次输入管理员密码" style="padding: 10px 0" show-password required></el-input>
                    <span slot="footer" class="dialog-footer">
                        <el-button type="primary" @click="regAdmin">添加</el-button>
                    </span>
                </el-dialog>

                <el-dialog
                    title="修改管理员"
                    :visible.sync="updateAdminRegVisible"
                    width="30%"
                >
                    <span style="margin-left: 10px">管理员账号</span>
                    <el-input v-model="updateModel.accountNumber" minlength="5" maxlength="20"
                              placeholder="请输入管理员账号" style="padding: 10px 0"
                              clearable required></el-input>
                    <span style="margin-left: 10px">管理员名称</span>
                    <el-input v-model="updateModel.adminName"  maxlength="8" placeholder="请输入管理员名称" style="padding: 10px 0" clearable required></el-input>
                    <span style="margin-left: 10px">手机号</span>
                    <el-input v-model="updateModel.phone" minlength="11" maxlength="11" placeholder="请输入11位手机号" style="padding: 10px 0" clearable required></el-input>
                    <span style="margin-left: 10px">邮箱</span>
                    <el-input v-model="updateModel.email" placeholder="请输入邮箱地址" style="padding: 10px 0" clearable required></el-input>
                    <span style="margin-left: 10px">性别</span>
                    <el-select v-model="updateModel.sex" placeholder="请选择性别" style="width: 80%; margin-left: 10px; padding: 10px 0" required>
                        <el-option label="男" value="男"></el-option>
                        <el-option label="女" value="女"></el-option>
                    </el-select><br>
                    <span style="margin-left: 10px">出生日期</span>
                    <el-date-picker
                        v-model="updateModel.birth"
                        type="date"
                        placeholder="选择出生日期"
                        style="width: 80%; margin-left: 10px; padding: 10px 0"
                        required>
                    </el-date-picker>
                    <span style="margin-left: 10px">管理员密码</span>
                    <el-input v-model="updateModel.adminPassword" minlength="8" placeholder="请输入管理员密码" style="padding: 10px 0" show-password required></el-input>
                    <span style="margin-left: 10px">确认管理员密码</span>
                    <el-input v-model="updateModel.adminRePassword" minlength="8" placeholder="请再次输入管理员密码" style="padding: 10px 0" show-password required></el-input>
                    <span slot="footer" class="dialog-footer">
                        <el-button type="primary" @click="updateAdmin(updateModel)">修改</el-button>
                    </span>
                </el-dialog>
            </div>
        </el-menu>

        <!-- 正常用户表格 -->
        <el-table v-show="this.mode == 1"
                  :data="userData"
                  stripe
                  style="width: 100%;color: #5a5c61;">
            <el-table-column label="头像" width="80">
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="48" :src="scope.row.avatar"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column
                prop="phone"
                label="手机号"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="email"
                label="邮箱"
                show-overflow-tooltip
                min-width="200"
                width="200">
            </el-table-column>
            <el-table-column
                prop="nickname"
                label="用户昵称"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="sex"
                label="性别"
                show-overflow-tooltip
                width="80">
                <template slot-scope="scope">
                    {{ scope.row.sex === '男' ? '男' : '女' }}
                </template>
            </el-table-column>
            <el-table-column
                prop="birth"
                label="出生日期"
                show-overflow-tooltip
                width="150">
                <template slot-scope="scope">
                    {{ scope.row.birth || '未填写' }}
                </template>
            </el-table-column>
            <el-table-column
                prop="signInTime"
                label="注册时间"
                show-overflow-tooltip
                width="200">
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="danger"
                        @click="sealUser(scope.row.id)">封号</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 违规用户表格 -->
        <el-table v-show="this.mode == 2"
                  :data="badUserData"
                  stripe
                  style="width: 100%;color: #5a5c61;">
            <el-table-column
                label="头像"
                width="80">
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="48" :src="scope.row.avatar"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column
                prop="phone"
                label="手机号"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="email"
                label="邮箱"
                show-overflow-tooltip
                min-width="200"
                width="200">
            </el-table-column>
            <el-table-column
                prop="nickname"
                label="用户昵称"
                show-overflow-tooltip
                width="150">
            </el-table-column>
            <el-table-column
                prop="sex"
                label="性别"
                show-overflow-tooltip
                width="80">
                <template slot-scope="scope">
                    {{ scope.row.sex === '男' ? '男' : '女' }}
                </template>
            </el-table-column>
            <el-table-column
                prop="birth"
                label="出生日期"
                show-overflow-tooltip
                width="150">
                <template slot-scope="scope">
                    {{ scope.row.birth || '未填写' }}
                </template>
            </el-table-column>
            <el-table-column
                prop="signInTime"
                label="注册时间"
                show-overflow-tooltip
                width="200">
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="success"
                        @click="unsealUser(scope.row.id)">解封</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 管理员表格 -->
        <el-table  v-show="this.mode == 3"
                   :data="userManage"
                   stripe
                   style="width: 100%;color: #5a5c61;">
            <el-table-column
                prop="accountNumber"
                label="管理员账号"
                show-overflow-tooltip
                width="180">
            </el-table-column>
            <el-table-column
                prop="id"
                label="编码"
            >
            </el-table-column>
            <el-table-column
                prop="phone"
                label="手机号"
                show-overflow-tooltip
                width="150">
            </el-table-column>
            <el-table-column
                prop="email"
                label="邮箱"
                show-overflow-tooltip
                width="200">
            </el-table-column>
            <el-table-column
                prop="adminName"
                label="管理名称"
            >
            </el-table-column>
            <el-table-column
                prop="sex"
                label="性别"
                show-overflow-tooltip
                width="80">
                <template slot-scope="scope">
                    {{ scope.row.sex === '男' ? '男' : '女' }}
                </template>
            </el-table-column>
            <el-table-column
                prop="birth"
                label="出生日期"
                show-overflow-tooltip
                width="150">
                <template slot-scope="scope">
                    {{ scope.row.birth || '未填写' }}
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="danger"
                        @click="getDetail(scope.row.id)">修改</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="block">
            <el-pagination
                @current-change="handleCurrentChange"
                :current-page.sync="nowPage"
                :page-size="8"
                background
                layout="prev, pager, next,jumper"
                :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>

export default {
    name: "userList",
    created() {
        this.getUserData();
    },
    methods: {
        handleCurrentChange(val) {
            this.nowPage = val;
            if(this.mode == 1){
                this.getUserData();
            }
            if(this.mode == 2){
                this.getBadUserData();
            }
            if(this.mode == 3){
                this.getUserManage();
            }
        },
        handleSelect(val){
            if(this.mode !== val){
                this.mode = val
                if(val == 1){
                    this.nowPage = 1;
                    this.getUserData();
                }
                if(val == 2){
                    this.nowPage = 1;
                    this.getBadUserData();
                }
                if(val == 3){
                    this.nowPage = 1;
                    this.getUserManage();
                }
            }
        },
        getDetail(id){
            this.updateAdminRegVisible = true;
            this.$api.getDetail({
                id: id,
            }).then(res =>{
                if(res.status_code==1){
                    // 转换日期格式
                    if(res.data.birth){
                        res.data.birth = new Date(res.data.birth).toLocaleDateString();
                    }
                    // 处理账号字段
                    this.updateModel.accountNumber = res.data.accountNumber || '';
                    // 将数字性别转换为字符串
                    if (res.data.sex !== undefined) {
                        res.data.sex = res.data.sex === '男' ? '男' : '女';
                    }
                    this.updateModel = res.data;
                }
            }).catch(error => {
                console.error('获取管理员详情失败:', error);
                this.$message.error('获取管理员信息失败');
            });
        },
        getUserData(){
            //正常普通用户
            this.$api.getUserData({
                page: this.nowPage,
                nums:8,
                status:0
            }).then(res => {
                if(res.status_code==1){
                    // 转换日期格式
                    res.data.list.forEach(item => {
                        if(item.birth){
                            item.birth = new Date(item.birth).toLocaleDateString();
                        }
                        // 确保性别显示正确
                        if (item.sex !== undefined) {
                            item.sex = item.sex === '男' ? '男' : '女';
                        }
                    });
                    this.userData = res.data.list;
                    this.total = res.data.count;
                }else {
                    this.$message.error(res.msg)
                }
            }).catch(e => {
                console.log(e)
            })
        },
        getBadUserData(){
            //违规用户
            this.$api.getUserData({
                page: this.nowPage,
                nums:8,
                status:1
            }).then(res => {
                if(res.status_code==1){
                    // 转换日期格式
                    res.data.list.forEach(item => {
                        if(item.birth){
                            item.birth = new Date(item.birth).toLocaleDateString();
                        }
                        // 确保性别显示正确
                        if (item.sex !== undefined) {
                            item.sex = item.sex === '男' ? '男' : '女';
                        }
                    });
                    this.badUserData = res.data.list;
                    this.total = res.data.count;
                }else {
                    this.$message.error(res.msg)
                }
            }).catch(e => {
                console.log(e)
            });
        },
        getUserManage(){
            //管理员
            this.$api.getUserManage({
                page: this.nowPage,
                nums:8,
            }).then(res => {
                if(res.status_code==1){
                    // 转换日期格式
                    res.data.list.forEach(item => {
                        if(item.birth){
                            item.birth = new Date(item.birth).toLocaleDateString();
                        }
                        // 确保性别显示正确
                        if (item.sex !== undefined) {
                            item.sex = item.sex === '男' ? '男' : '女';
                        }
                    });
                    this.userManage = res.data.list;
                    this.total = res.data.count;
                }else {
                    this.$message.error(res.msg)
                }
            }).catch(e => {
                console.log(e)
            })
        },
        // 封号方法，接收用户ID
        sealUser(userId) {
            // 验证用户ID是否有效
            if (!userId || isNaN(userId) || userId <= 0) {
                this.$message.error('无效的用户ID');
                return;
            }

            this.$confirm('确定要封号吗？此操作不可恢复', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 调用后端接口，传递用户ID和状态
                return this.$api.updateUserStatus({
                    id: userId,
                    status: 1
                });
            })
                .then(res => {
                    if (res.status_code === 1) {
                        this.$message.success('封号成功');
                        this.getUserData(); // 刷新用户列表
                    } else {
                        this.$message.error(res.msg);
                    }
                })
                .catch(() => {
                    this.$message.info('已取消封号');
                });
        },
        // 解封方法，接收用户ID
        unsealUser(userId){
            // 验证用户ID是否有效
            if (!userId || isNaN(userId) || userId <= 0) {
                this.$message.error('无效的用户ID');
                return;
            }

            this.$confirm('确定要解封吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 调用后端接口，传递用户ID和状态
                return this.$api.updateUserStatus({
                    id: userId,
                    status: 0
                });
            })
                .then(res => {
                    if (res.status_code === 1) {
                        this.$message.success('解封成功');
                        this.getBadUserData(); // 刷新违规用户列表
                    } else {
                        this.$message.error(res.msg);
                    }
                })
                .catch(() => {
                    this.$message.info('已取消解封');
                });
        },
        formatDate(date) {
            if (!date) return '';
            const d = new Date(date);
            return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;
        },
        regAdmin(){
            // 表单验证
            if (!this.adminAccountNumber) {
                this.$message.error('管理员账号不能为空');
                return;
            }
            if (!this.adminName) {
                this.$message.error('管理员名称不能为空');
                return;
            }
            if (!this.adminPhone || this.adminPhone.length !== 11) {
                this.$message.error('请输入有效的11位手机号');
                return;
            }
            if (!this.adminEmail || !this.validateEmail(this.adminEmail)) {
                this.$message.error('请输入有效的邮箱地址');
                return;
            }
            if (this.adminSex === '') {
                this.$message.error('请选择性别');
                return;
            }
            if (!this.adminBirth) {
                this.$message.error('请选择出生日期');
                return;
            }
            if (!this.adminPassword || this.adminPassword.length < 8) {
                this.$message.error('密码长度不能少于8位');
                return;
            }
            if (this.adminPassword !== this.adminRePassword) {
                this.$message.error('两次输入的密码不一致');
                return;
            }

            // 准备提交的数据
            const formData = {
                accountNumber: this.adminAccountNumber,
                adminName: this.adminName,
                phone: this.adminPhone,
                email: this.adminEmail,
                sex: this.adminSex, // 性别值现在是字符串 "男" 或 "女"
                birth: this.formatDate(this.adminBirth),
                adminPassword: this.adminPassword,
            };

            // 添加调试日志
            console.log('提交的表单数据:', formData);

            this.$api.regAdministrator(formData)
                .then(res => {
                    if (res.status_code === 1) {
                        this.total = this.total + 1;
                        this.nowPage = Math.ceil(this.total / 8);
                        this.getUserManage();
                        this.adminRegVisible = false;
                        this.resetAdminForm(); // 重置表单
                    } else {
                        this.$message.error(res.msg);
                    }
                })
                .catch(e => {
                    console.error('添加管理员失败:', e);
                    this.$message.error('添加失败，账号重复或网络异常');
                });
        },
        resetAdminForm() {
            this.adminAccountNumber = '';
            this.adminName = '';
            this.adminPhone = '';
            this.adminEmail = '';
            this.adminSex = '';
            this.adminBirth = '';
            this.adminPassword = '';
            this.adminRePassword = '';
        },
        validateEmail(email) {
            const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return re.test(email);
        },
        updateAdmin(updateModel){
            // 表单验证
            if (!updateModel.accountNumber) {
                this.$message.error('管理员账号不能为空');
                return;
            }
            if (!updateModel.adminName) {
                this.$message.error('管理员名称不能为空');
                return;
            }
            if (!updateModel.phone || updateModel.phone.length !== 11) {
                this.$message.error('请输入有效的11位手机号');
                return;
            }
            if (!updateModel.email || !this.validateEmail(updateModel.email)) {
                this.$message.error('请输入有效的邮箱地址');
                return;
            }
            if (!updateModel.sex) {
                this.$message.error('请选择性别');
                return;
            }
            if (!updateModel.birth) {
                this.$message.error('请选择出生日期');
                return;
            }
            if (!updateModel.adminPassword || updateModel.adminPassword.length < 5) {
                this.$message.error('密码长度不能少于8位');
                return;
            }
            if (updateModel.adminPassword !== updateModel.adminRePassword) {
                this.$message.error('两次输入的密码不一致');
                return;
            }

            // 确保性别值是字符串
            if (updateModel.sex !== undefined) {
                updateModel.sex = updateModel.sex === '男' ? '男' : '女';
            }

            // 格式化日期
            if (updateModel.birth) {
                updateModel.birth = this.formatDate(updateModel.birth);
            }

            this.$api.updateAdmin(updateModel)
                .then(res => {
                    if (res.status_code === 1) {
                        this.updateAdminRegVisible = false;
                        this.getUserManage();
                    } else {
                        this.$message.error(res.msg);
                    }
                })
                .catch(e => {
                    console.error('修改管理员失败:', e);
                    this.$message.error('修改失败，账号重复或网络异常');
                });
        },
        searchIdle() {
            this.$api.queryUser({
                searchValue: this.searchValue,
                mode: this.mode,
                page: this.nowPage,
                nums: 8,
            }).then(res => {
                if (res.status_code == 1) {
                    // 转换日期格式
                    if(res.data.list && res.data.list.length > 0){
                        res.data.list.forEach(item => {
                            if(item.birth){
                                item.birth = new Date(item.birth).toLocaleDateString();
                            }
                            // 确保性别显示正确
                            if (item.sex !== undefined) {
                                item.sex = item.sex === "男" ? '男' : '女';
                            }
                        });
                    }

                    if(this.mode == 1){
                        this.userData = res.data.list;
                        this.total = res.data.count;
                    }else if(this.mode == 2){
                        this.badUserData = res.data.list;
                        this.total = res.data.count;
                    }else {
                        this.userManage = res.data.list;
                        this.total = res.data.count;
                    }
                } else{
                    this.$message.error(res.msg)
                }
            }).catch(e => {
                console.log(e)
            })
        }
    },
    data(){
        return {
            mode:1,
            nowPage: 1,
            total: 63,
            adminRegVisible: false,
            updateAdminRegVisible: false,
            adminAccountNumber: '',
            adminPhone:'',
            adminEmail:'',
            adminSex: '',
            adminBirth: '',
            adminPassword:'',
            adminRePassword:'',
            adminName:'',
            userData: [],
            badUserData:[],
            userManage:[],
            searchValue: '',
            updateModel: {
                accountNumber: '',
                adminName: '',
                phone: '',
                email: '',
                sex: '',
                birth: '',
                adminPassword: '',
                adminRePassword: ''
            }
        }
    },
}
</script>

<style scoped>
.main-border{
    background-color: #FFF;
    padding: 10px 30px;
    box-shadow: 0 1px 15px -6px rgba(0,0,0,.5);
    border-radius: 5px;
}
.block {
    display: flex;
    justify-content:center;
    padding-top: 15px;
    padding-bottom: 10px;
    width: 100%;
}
.addAdminButton{
    display:flex;
    justify-content: flex-end;
    align-items: center;
    height: 60px;
    outline: none;
}
/* 可选：如果需要进一步调整头像样式，可以添加以下样式 */
.el-avatar {
    margin-right: 5px; /* 头像与其他内容的间距 */
}
</style>
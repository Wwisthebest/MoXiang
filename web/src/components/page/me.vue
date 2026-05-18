<template>
    <div>
        <app-head :nickname-value="userInfo.nickname"
                  :avatarValue="userInfo.avatar"></app-head>
        <app-body>
            <div v-show="!eidtAddress">
                <div class="user-info-container">
                    <div class="user-info-details">
                        <el-upload
                            action="http://localhost:8888/file/"
                            :on-success="fileHandleSuccess"
                            :file-list="imgFileList"
                            accept="image/*">
                            <el-image
                                style="width: 120px; height: 120px;border-radius: 10px;"
                                :src="userInfo.avatar"
                                fit="contain"></el-image>
                        </el-upload>
                        <div class="user-info-details-text">
                            <div class="user-info-details-text-nickname">{{userInfo.nickname}}</div>
                            <div class="user-info-details-text-time">{{userInfo.signInTime}} 加入平台</div>
                            <div class="user-info-details-text-edit">
                                <el-button type="primary" plain @click="userInfoDialogVisible = true">编辑个人信息</el-button>
                            </div>

                            <el-dialog
                                @close="finishEdit"
                                title="编辑个人信息"
                                :visible.sync="userInfoDialogVisible"
                                width="400px">

                                <div class="edit-tip">头像</div>
                                <div class="avatar-edit-container">
                                    <el-upload
                                        action="http://localhost:8888/file/"
                                        :on-success="handleAvatarSuccess"
                                        :file-list="avatarFileList"
                                        accept="image/*"
                                        list-type="picture">
                                        <el-image
                                            v-if="userInfo.avatar"
                                            :src="userInfo.avatar"
                                            fit="cover"
                                            class="avatar-preview">
                                        </el-image>
                                        <div v-else class="avatar-placeholder">
                                            <i class="el-icon-user-solid"></i>
                                            <p>未设置头像</p>
                                        </div>
                                    </el-upload>
                                </div>

                                <div class="edit-tip">昵称</div>
                                <el-input
                                    v-model="userInfo.nickname"
                                    :disabled="notUserNicknameEdit"
                                    @change="saveUserNickname">
                                    <el-button slot="append" type="warning" icon="el-icon-edit"
                                               @click="notUserNicknameEdit = false">编辑
                                    </el-button>
                                </el-input>

                                <!-- 新增邮件编辑区域 -->
                                <div class="edit-tip">邮箱</div>
                                <el-input
                                    v-model="userInfo.email"
                                    :disabled="notUserEmailEdit"
                                    @change="saveUserInfo">
                                    <el-button slot="append" type="warning" icon="el-icon-edit"
                                               @click="notUserEmailEdit = false">编辑
                                    </el-button>
                                </el-input>


                                <div class="edit-tip">手机号</div>
                                <el-input
                                    v-model="userInfo.phone"
                                    :disabled="notUserPhoneEdit"
                                    @change="saveUserInfo">
                                    <el-button slot="append" type="warning" icon="el-icon-edit"
                                               @click="notUserPhoneEdit = false">编辑
                                    </el-button>
                                </el-input>

                                <!-- 性别选择器 -->
                                <div class="edit-tip">性别</div>
                                <el-select
                                    v-model="userInfo.sex"
                                    placeholder="请选择性别"
                                    @change="saveUserInfo">
                                    <el-option label="男" value="男"></el-option>
                                    <el-option label="女" value="女"></el-option>
                                </el-select>

                                <!-- 生日选择器 -->
                                <div class="edit-tip">出生日期</div>
                                <el-date-picker
                                    v-model="userInfo.birth"
                                    type="date"
                                    placeholder="选择日期"
                                    value-format="yyyy-MM-dd"
                                    @change="saveUserInfo">
                                </el-date-picker>

                                <div v-if="userPasswordEdit">
                                    <div class="edit-tip">原密码</div>
                                    <el-input v-model="userPassword1" show-password></el-input>
                                    <div class="edit-tip">新密码</div>
                                    <el-input v-model="userPassword2" show-password></el-input>
                                    <div class="edit-tip">确认新密码</div>
                                    <el-input v-model="userPassword3" show-password></el-input>
                                    <div class="edit-tip"></div>
                                    <el-button @click="savePassword" plain>确认修改</el-button>
                                </div>
                                <div v-else>
                                    <div class="edit-tip">密码</div>
                                    <el-input
                                        value="123456"
                                        :disabled="true"
                                        show-password>
                                        <el-button slot="append" type="warning" icon="el-icon-edit"
                                                   @click="userPasswordEdit = true">编辑
                                        </el-button>
                                    </el-input>
                                </div>
                                <span slot="footer" class="dialog-footer">
                                <el-button @click="userInfoDialogVisible=false">完成</el-button>
                            </span>
                            </el-dialog>
                        </div>
                    </div>
                    <div class="user-info-splace">
                        <el-button type="primary" plain @click="eidtAddress=true">编辑收货地址</el-button>
                    </div>
                </div>
                <div class="idle-container">
                    <el-tabs v-model="activeName" @tab-click="handleClick">
                        <!-- 将"我的购物车"移到第一个位置，其他标签页顺序后移 -->
                        <el-tab-pane label="我的购物车" name="3">
                            <!-- 加载状态提示 -->
                            <div v-if="loading && !isCartLoaded" class="loading-container">
                                <i class="el-icon-loading"></i> 加载中...
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="刚刚发布" name="1"></el-tab-pane>
                        <el-tab-pane label="已经下架" name="2"></el-tab-pane>
                        <el-tab-pane label="出售记录" name="4"></el-tab-pane>
                        <el-tab-pane label="购买记录" name="5"></el-tab-pane>
                    </el-tabs>
                    <div class="idle-container-list">
                        <!-- 添加@click事件，并传递activeName和item -->
                        <!-- 修改列表项点击事件，添加条件判断 -->
                        <div
                            v-for="(item, index) in getTabData(activeName)"
                            class="idle-container-list-item"
                            :key="item.id || index"
                            @click="handleItemClick(activeName, item, $event)"
                        >
                            <div class="idle-container-list-item-detile">
                                <el-checkbox
                                    v-if="activeName === '3'"
                                    v-model="item.selected"
                                    @change="handleCheckboxChange(item)"
                                    @click.native.stop="handleCheckboxClick"
                                ></el-checkbox>

                                <!-- 商品图片 -->
                                <el-image
                                    style="width: 100px; height: 100px;"
                                    :src="item.imgUrl"
                                    fit="cover">
                                    <div slot="error" class="image-slot">
                                        <i class="el-icon-picture-outline">无图</i>
                                    </div>
                                </el-image>
                                <div class="idle-container-list-item-text">
                                    <!-- 商品标题 -->
                                    <div class="idle-container-list-title">
                                        {{item.idleName || item.orderTitle}}
                                    </div>
                                    <!-- 商品详情 -->
                                    <div class="idle-container-list-idle-details" v-html="item.idleDetails || item.orderDesc">
                                        {{item.idleDetails || item.orderDesc}}
                                    </div>
                                    <!-- 商品发布时间 -->
                                    <div class="idle-container-list-idle-time">{{item.timeStr || item.createTime}}</div>

                                    <div class="idle-item-foot">
                                        <!-- 商品价格 -->
                                        <div class="idle-prive">
                                            ￥{{item.idlePrice || item.orderAmount}}
                                        </div>
                                        <!-- 操作按钮 -->
                                        <el-button v-if="activeName === '1'" type="primary" size="mini" slot="reference"
                                                   plain @click.stop="handleEditItem(item)"><i class="el-icon-edit"></i> 编辑
                                        </el-button>
                                        <el-button v-if="activeName === '1'" type="danger" size="mini" slot="reference"
                                                   plain @click.stop="handle(activeName, item, index)">下架
                                        </el-button>
                                        <el-button v-if="activeName === '2'" type="success" size="mini" slot="reference"
                                                   plain @click.stop="handle(activeName, item, index)">上架
                                        </el-button>
                                        <el-button v-if="activeName === '2'" type="danger" size="mini" slot="reference"
                                                   plain @click.stop="handleDeleteItem(item, index)">删除
                                        </el-button>
                                        <el-button v-if="activeName === '3'" type="danger" size="mini" slot="reference"
                                                   plain @click.stop="handle(activeName, item, index)">取消购物车
                                        </el-button>
                                    </div>
                                </div>
                            </div>
                        </div>


                        <!-- 结算栏（放在列表内部） -->
                        <div class="cart-checkout-bar" v-if="activeName === '3' && getTabData(activeName).length > 0">
                            <div class="cart-select-all" v-if="activeName === '3'">
                                <el-checkbox v-model="isSelectAll" @change="selectAllCartItems">全选</el-checkbox>
                            </div>
                            <div class="cart-total-price">
                                合计：<span class="total-amount">￥{{cartTotalPrice.toFixed(2)}}</span>
                            </div>
                            <el-button type="primary" @click="checkoutCart">结算({{cartSelected.length}})</el-button>
                        </div>
                    </div>
                </div>
            </div>

            <div v-show="eidtAddress" class="address-container">
                <el-page-header class="address-container-back" @back="eidtAddress=false"
                                content="收货地址"></el-page-header>
                <div class="address-container-add">
                    <div class="address-container-add-title">新增收货地址</div>
                    <div class="address-container-add-item">
                        <el-input placeholder="请输入收货人姓名" v-model="addressInfo.consigneeName" maxlength="10"
                                  show-word-limit>
                            <div slot="prepend">收货人姓名</div>
                        </el-input>
                    </div>
                    <div class="address-container-add-item">
                        <el-input placeholder="请输入收货人手机号" v-model="addressInfo.consigneePhone"
                                  onkeyup="this.value=this.value.replace(/[^\d.]/g,'');" maxlength="11" show-word-limit>
                            <div slot="prepend">您的手机号</div>
                        </el-input>
                    </div>

                    <div class="address-container-add-item">
                        <span class="demonstration">宿舍类型/楼号/楼层</span>
                        <el-cascader
                            :options="options"
                            v-model="selectedOptions"
                            @change="handleAddressChange"
                            :separator="' '"
                        >
                        </el-cascader>
                    </div>
                    <div class="address-container-add-item">
                        <el-input placeholder="宿舍号 + 在楼道东侧还是西侧还是中间位置" v-model="addressInfo.detailAddress"
                                  maxlength="50" show-word-limit>
                            <div slot="prepend">详细地址</div>
                        </el-input>
                    </div>
                    <el-checkbox v-model="addressInfo.defaultFlag">设置为默认地址</el-checkbox>
                    <el-button style="margin-left: 20px;" @click="saveAddress">保存</el-button>
                </div>
                <div class="address-container-list">
                    <div style="color: #409EFF;font-size: 15px;padding-left: 10px;">已有收货地址</div>
                    <el-table
                        stripe
                        :data="addressData"
                        style="width: 100%">
                        <el-table-column
                            prop="consigneeName"
                            label="收货人姓名"
                            width="100">
                        </el-table-column>
                        <el-table-column
                            prop="consigneePhone"
                            label="手机号"
                            width="120">
                        </el-table-column>
                        <el-table-column
                            prop="detailAddressText"
                            label="地址"
                            width="270">
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                    size="mini"
                                    @click="handleEdit(scope.$index, scope.row)">编辑
                                </el-button>
                                <el-button
                                    size="mini"
                                    type="danger"
                                    @click="handleDelete(scope.$index, scope.row)">删除
                                </el-button>
                            </template>
                        </el-table-column>
                        <el-table-column label="是否默认地址" width="110">
                            <template slot-scope="scope">
                                <el-button v-if="!scope.row.defaultFlag"
                                           size="mini"
                                           @click="handleSetDefault(scope.$index, scope.row)">设为默认
                                </el-button>
                                <div v-else style="padding-left: 10px;color: #409EFF;">{{scope.row.defaultAddress}}
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
            <el-dialog
                @close="closeEditDialog"
                title="编辑闲置信息"
                :visible.sync="editDialogVisible"
                width="600px">
                <div class="release-idle-container-form">
                    <el-input placeholder="请输入闲置/公告标题" v-model="editIdleItemInfo.idleName"
                              maxlength="30"
                              show-word-limit>
                    </el-input>
                    <el-input
                        class="release-idle-detiles-text"
                        type="textarea"
                        autosize
                        placeholder="请输入闲置/公告的详细介绍..."
                        v-model="editIdleItemInfo.idleDetails"
                        maxlength="1000"
                        show-word-limit>
                    </el-input>
                    <div class="release-idle-place">
                        <div class="release-tip">您的位置</div>
                        <el-cascader
                            :options="options"
                            v-model="selectedOptions"
                            @change="handleAddressChange"
                            :separator="' '"
                            style="width: 90%;"
                        >
                        </el-cascader>
                    </div>
                    <div style="display: flex; justify-content: space-between;">
                        <div>
                            <div class="release-tip">闲置类别</div>
                            <el-select v-model="editIdleItemInfo.idleLabel" placeholder="请选择类别">
                                <el-option
                                    v-for="item in options2"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </div>
                        <div v-show="editIdleItemInfo.idleLabel !== 5" style="width: 300px;">
                            <el-input-number v-model="editIdleItemInfo.idlePrice" :precision="2" :step="10" :max="10000000">
                                <div slot="prepend">价格</div>
                            </el-input-number>
                        </div>
                    </div>
                    <div class="release-idle-container-picture">
                        <div class="release-idle-container-picture-title">上传闲置照片</div>
                        <el-upload
                            ref="imageUpload"
                            action="http://localhost:8888/file/"
                            :on-preview="fileHandlePreview"
                            :on-remove="editFileHandleRemove"
                            :on-success="editFileHandleSuccess"
                            :file-list="fileList"
                            :limit="10"
                            :on-exceed="handleExceed"
                            accept="image/*"
                            drag
                            multiple
                            list-type="picture">
                            <i class="el-icon-upload"></i>
                            <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
                        </el-upload>
                        <div class="picture-list">
                            <div class="edit-image-grid">
                                <!-- 显示已上传的图片 -->
                                <div v-for="(img, index) in editImgList" :key="index" class="image-item">
                                    <div class="image-wrapper">
                                        <el-image
                                            :src="img"
                                            :preview-src-list="editImgList"
                                            fit="cover"
                                            class="uploaded-image">
                                            <div slot="error" class="image-slot">
                                                <i class="el-icon-picture-outline">无图</i>
                                            </div>
                                        </el-image>
                                        <div class="image-overlay">
                                            <i class="el-icon-delete" @click="handleImageRemove(index)"></i>
                                        </div>
                                    </div>
                                    <div class="image-name">
                                        {{ 'image-' + (index + 1) }}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <el-dialog :visible.sync="imgDialogVisible">
                            <img width="100%" :src="dialogImageUrl" alt="">
                        </el-dialog>
                    </div>
                </div>
                <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
    </span>
            </el-dialog>
            <app-foot></app-foot>
        </app-body>
    </div>
</template>

<script>
import AppHead from '../common/AppHeader.vue';
import AppBody from '../common/AppPageBody.vue'
import AppFoot from '../common/AppFoot.vue'
import options from '../common/country-data.js'


export default {
    name: "me",
    components: {
        AppHead,
        AppBody,
        AppFoot
    },
    data() {
        return {
            isCheckboxClicked: false,
            options2: [{
                value: 1,
                label: '文学艺术'
            }, {
                value: 2,
                label: '人文社科'
            }, {
                value: 3,
                label: '经济管理'
            }, {
                value: 4,
                label: '生活休闲'
            }, {
                value: 5,
                label: '公告'
            }],
            imgFileList: [],
            addressInfo: {
                consigneeName: '',
                consigneePhone: '',
                provinceName: '',
                cityName: '',
                regionName: '',
                detailAddress: '',
                defaultFlag: false
                },
            // 将这些变量移到顶级位置
            editDialogVisible: false,
            editIdleItemInfo: {
                idleName: '',
                idleDetails: '',
                pictureList: '',
                idlePrice: 0,
                idlePlace: '',
                idleLabel: ''
            },
            editImgList: [],
            activeName: '3', // 默认显示"我的购物车"
            cartSelected: [], // 存储选中的购物车商品ID
            cartTotalPrice: 0, // 选中商品总价
            handleName: ['下架', '删除', '取消购物车', '', ''],
            dataList: [
                [], // 刚刚发布
                [], // 已经下架
                [], // 我的购物车
                [], // 出售记录
                [], // 购买记录
                [],
                [],
                [],
            ],
            orderStatus: ['待付款', '待发货', '待收货', '已完成', '已取消'],
            userInfoDialogVisible: false,
            notUserNicknameEdit: true,
            userPasswordEdit: false,
            notUserEmailEdit: true, // 邮箱编辑状态控制
            notUserPhoneEdit: true, // 手机编辑状态控制
            userPassword1: '',
            userPassword2: '',
            userPassword3: '',
            eidtAddress: false,
            selectedOptions: [],//存放默认值
            options: options,   //存放城市数据,
            userInfo: {
                accountNumber: "",
                avatar: "",
                nickname: "",
                signInTime: "",
                sex: "", // 性别
                birth: "", // 出生日期
                email: "" ,// 新增邮箱字段
                phone: "", // 新增手机字段

            },
            addressData: [],
            isSelectAll: false, // 全选状态
            loading: false, // 加载状态
            payResult: null, // 支付结果
            isCartLoaded: false, // 购物车数据加载状态
            isIdleLoaded: false, // 闲置物品数据加载状态
        };
    },
    created() {
        // 确保用户信息存在，特别是用户ID
        if (!this.$globalData.userInfo.id) {
            this.$api.getUserInfo().then(res => {
                if (res.status_code === 1) {
                    // 处理从后端获取的用户信息
                    res.data.signInTime = res.data.signInTime.substring(0, 10);
                    console.log('用户信息:', res.data);

                    // 保存到全局数据
                    this.$globalData.userInfo = res.data;
                    this.userInfo = this.$globalData.userInfo;

                    // 继续加载其他数据
                    this.loadAllData();
                } else {
                    console.error('获取用户信息失败:', res);
                    // 可以添加提示或跳转到登录页
                }
            }).catch(error => {
                console.error('获取用户信息请求错误:', error);
                // 处理网络错误
            });
        } else {
            // 如果全局数据中已有用户信息，直接使用
            this.userInfo = this.$globalData.userInfo;
            console.log('使用缓存的用户信息:', this.userInfo);

            // 加载其他数据
            this.loadAllData();
        }
    },
    activated() {
        // 组件激活时刷新数据（处理缓存问题）
        if (this.activeName === '1' || this.activeName === '2') {
            this.getIdleItemData();
        } else if (this.activeName === '3') {
            this.getMyFavorite(); // 加载购物车数据
        }
    },
    computed: {
        isSelectAll: {
            get() {
                return this.dataList[2].length > 0 && this.dataList[2].every(item => item.selected);
            },
            set(value) {
                this.selectAllCartItems(value);
            }
        }
    },
    methods: {

        handleDeleteItem(item, index) {
            console.log('删除商品:', item, index);
            this.loading = true; // 显示加载状态

            this.$confirm('是否确认删除该商品？', '提示', {
                confirmButtonText: '确认删除',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$api.updateIdleItem({
                    id: item.id,
                    idleStatus: 0
                }).then(res => {
                    if (res.status_code === 1) {
                        // 方法1：直接从数组中删除（适合单条删除）
                        this.dataList[1].splice(index, 1);

                        // 方法2：重新加载数据（适合复杂数据或批量操作）
                        this.loadIdleData();

                        this.$message.success('商品已删除');
                    } else {
                        this.$message.error(res.msg || '删除失败，请稍后再试');
                    }
                })
                    .catch(error => {
                        console.error('删除请求异常:', error);
                        this.$message.error('网络错误，删除失败');
                    })
                    .finally(() => {
                        this.loading = false;
                    });
            })
                .catch(() => {
                    this.loading = false;
                });
        },

// 重新加载已下架商品数据
        loadIdleData() {
            this.$api.getAllIdleItem({
                userId: this.userInfo.id,
                status: 2 // 已下架状态码
            }).then(res => {
                if (res.status_code === 1) {
                    this.dataList[1] = []; // 清空现有数据
                    for (let item of res.data) {
                        // 处理数据格式
                        item.timeStr = item.releaseTime.substring(0, 10) + " " + item.releaseTime.substring(11, 19);
                        item.imgUrl = JSON.parse(item.pictureList)[0] || '';
                        this.dataList[1].push(item);
                    }
                } else {
                    this.$message.error('数据加载失败');
                }
            })
                .catch(error => {
                    console.error('加载数据异常:', error);
                    this.$message.error('网络错误，数据加载失败');
                });
        },


// 处理复选框点击
            handleCheckboxClick(event) {
                event.stopPropagation(); // 彻底阻止事件冒泡
            },

    // 处理复选框变化
            handleCheckboxChange(item) {
                item.selected = !item.selected; // 切换选中状态
                this.calculateCartTotal(); // 重新计算总价
            },
    // 处理列表项点击
            handleItemClick(activeName, item, event) {
                // 如果点击的是复选框或复选框的标签，则不跳转
                if (event.target.closest('.el-checkbox') || event.target.classList.contains('el-checkbox__input')) {
                    return;
                }

                // 只有购物车和购买记录才跳转详情
                if (activeName === '3' || activeName === '5') {
                    this.toDetails(activeName, item);
                }
            },
        // 根据标签页名称获取对应数据
        getTabData(tabName) {
            const tabIndex = parseInt(tabName) - 1;
            return this.dataList[tabIndex] || [];
        },

        // 计算购物车总价
        calculateCartTotal() {
            this.cartSelected = this.dataList[2].filter(item => item.selected).map(item => item.id);
            this.cartTotalPrice = this.dataList[2].reduce((total, item) => {
                // 确保价格存在且为数字类型
                const price = parseFloat(item.idlePrice) || 0;
                return item.selected ? total + price : total;
            }, 0);
        },

        // 全选/全不选
        selectAllCartItems(value) {
            this.dataList[2].forEach(item => {
                item.selected = value;
            });
            this.calculateCartTotal();
        },

        // 购物车结算
        checkoutCart() {
            if (this.cartSelected.length === 0) {
                this.$message.warning('请选择要结算的商品');
                return;
            }

            this.$confirm(`确定要结算${this.cartSelected.length}件商品，总价￥${this.cartTotalPrice.toFixed(2)}吗？`, '结算确认', {
                confirmButtonText: '确认结算',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.loading = true;
                // 调用支付宝支付接口
                this.$api.post('/alipay/pay', {
                    favoriteIds: this.cartSelected
                }).then(res => {
                    if (res.status_code === 1) {
                        // 假设返回的是HTML表单，直接提交
                        const formElement = document.createElement('div');
                        formElement.innerHTML = res.data;
                        document.body.appendChild(formElement);
                        document.forms[0].submit();
                    } else {
                        this.$message.error(res.msg || '结算失败，请稍后再试');
                    }
                }).catch(error => {
                    console.error('结算请求失败', error);
                    this.$message.error('网络错误，请稍后再试');
                }).finally(() => {
                    this.loading = false;
                });
            }).catch(() => {
                // 取消结算
            });
        },

        // 处理支付结果回调
        handlePayResult(result) {
            this.payResult = result;
            if (result.success) {
                // 支付成功后，从购物车移除已结算商品
                this.dataList[2] = this.dataList[2].filter(item => !this.cartSelected.includes(item.id));
                // 重置选中状态
                this.isSelectAll = false;
                this.calculateCartTotal();
                this.$message.success('支付成功');
            } else {
                this.$message.error(result.message || '支付失败');
            }
        },

        // 处理头像上传成功
        handleAvatarSuccess(response, file, fileList) {
            console.log("头像上传成功:", response, file, fileList);
            const imgUrl = response.data;
            this.avatarFileList = []; // 上传成功后清空列表
            this.userInfo.avatar = imgUrl;
            this.$globalData.userInfo.avatar = imgUrl;

            // 调用API更新头像
            this.$api.updateUserPublicInfo({
                id: this.userInfo.id,
                avatar: imgUrl
            }).then(res => {
                if (res.status_code !== 1) {
                    this.$message.error('头像更新失败');
                }
            });
        },

        // 统一保存用户信息
        saveUserInfo() {
            if (!this.notUserNicknameEdit) {
                // 如果是昵称编辑状态，先锁定输入框
                this.notUserNicknameEdit = true;
            }
            if (!this.notUserEmailEdit) {
                this.notUserEmailEdit = true; // 编辑后锁定邮箱输入框
            }

            if(!this.notUserPhoneEdit){
                this.notUserPhoneEdit = true; // 编辑后锁定手机输入框
            }


            const userData = {
                id: this.userInfo.id,
                nickname: this.userInfo.nickname,
                sex: this.userInfo.sex,
                birth: this.userInfo.birth,
                email: this.userInfo.email,
                phone: this.userInfo.phone
            };

            this.$api.updateUserPublicInfo(userData)
                .then(res => {
                    this.$globalData.userInfo = { ...this.userInfo };
                });
        },

        // 加载所有数据
        loadAllData() {
            // 修改为串行加载数据，确保购物车数据优先加载
            this.getMyFavorite().then(() => {
                // 购物车数据加载完成后，再加载其他数据
                Promise.all([
                    this.getIdleItemData(),
                    this.getMySoldIdle(),
                    this.getMyOrder()
                ]).then(() => {
                    console.log('所有数据加载完成');
                }).catch(error => {
                    console.error('数据加载失败', error);
                });
            });
        },

        // 添加新的图片删除方法
        handleImageRemove(index) {
            // 从数组中移除指定索引的图片
            this.editImgList.splice(index, 1);
            this.$message.success('图片已删除');
        },

        // 修改图片预览方法以适应新的结构
        fileHandlePreview(file) {
            console.log('预览图片:', file);
            // 如果是编辑页面，使用editImgList
            if (this.editDialogVisible) {
                this.dialogImageUrl = file.response ? file.response.data : file.url;
            } else {
                // 保持原有的预览逻辑
                this.dialogImageUrl = file.response.data;
            }
            this.imgDialogVisible = true;
        },
        // 刷新闲置商品数据（刚刚发布和已下架）
        refreshIdleData() {
            this.getIdleItemData();
        },

        // 修改购物车数据加载方法，确保传递用户ID
        getMyFavorite() {
            return new Promise((resolve, reject) => {
                this.loading = true;
                this.$api.getMyFavorite({
                    userId: this.userInfo.id, // 明确传递用户ID
                    pageNum: 1,
                    pageSize: 100
                }).then(res => {
                    console.log('getMyFavorite', res);
                    if (res.status_code === 1) {
                        this.dataList[2] = []; // 清空购物车数据
                        for (let i = 0; i < res.data.length; i++) {
                            let pictureList = JSON.parse(res.data[i].idleItem.pictureList);
                            this.dataList[2].push({
                                favoriteId: res.data[i].id,
                                id: res.data[i].idleItem.id,
                                imgUrl: pictureList.length > 0 ? pictureList[0] : '',
                                idleName: res.data[i].idleItem.idleName,
                                idleDetails: res.data[i].idleItem.idleDetails,
                                timeStr: res.data[i].createTime.substring(0, 10) + " " + res.data[i].createTime.substring(11, 19),
                                idlePrice: res.data[i].idleItem.idlePrice,
                                selected: false // 初始化选中状态为false
                            });
                        }
                        this.isCartLoaded = true; // 标记购物车数据已加载
                        this.calculateCartTotal();
                        resolve();
                    } else {
                        console.error('获取购物车数据失败:', res);
                        this.dataList[2] = [];
                        this.isCartLoaded = true; // 即使失败也标记为已加载
                        resolve();
                    }
                }).catch(error => {
                    console.error('获取购物车数据请求错误:', error);
                    this.dataList[2] = [];
                    this.isCartLoaded = true; // 网络错误也标记为已加载
                    reject(error);
                }).finally(() => {
                    this.loading = false;
                });
            });
        },

        // 修改其他数据加载方法，确保传递用户ID
        getMySoldIdle() {
            return new Promise((resolve, reject) => {
                this.$api.getMySoldIdle({
                    userId: this.userInfo.id // 明确传递用户ID
                }).then(res => {
                    if (res.status_code === 1) {
                        this.dataList[3] = []; // 清空出售记录数据
                        console.log('getMySoldIdle', res.data);
                        for (let i = 0; i < res.data.length; i++) {
                            let pictureList = JSON.parse(res.data[i].idleItem.pictureList);
                            this.dataList[3].push({
                                id: res.data[i].id,
                                imgUrl: pictureList.length > 0 ? pictureList[0] : '',
                                idleName: res.data[i].idleItem.idleName,
                                idleDetails: res.data[i].idleItem.idleDetails,
                                timeStr: res.data[i].createTime.substring(0, 10) + " " + res.data[i].createTime.substring(11, 19),
                                idlePrice: res.data[i].orderPrice,
                                orderStatus: res.data[i].orderStatus
                            });
                        }
                        resolve();
                    } else {
                        reject(res);
                    }
                }).catch(error => {
                    reject(error);
                });
            });
        },

        getMyOrder() {
            return new Promise((resolve, reject) => {
                this.$api.getMyOrder({
                    userId: this.userInfo.id // 明确传递用户ID
                }).then(res => {
                    if (res.status_code === 1) {
                        this.dataList[4] = []; // 清空购买记录数据
                        console.log('getMyOrder', res.data);
                        for (let i = 0; i < res.data.length; i++) {
                            let pictureList = JSON.parse(res.data[i].idleItem.pictureList);
                            this.dataList[4].push({
                                id: res.data[i].id,
                                imgUrl: pictureList.length > 0 ? pictureList[0] : '',
                                idleName: res.data[i].idleItem.idleName,
                                idleDetails: res.data[i].idleItem.idleDetails,
                                timeStr: res.data[i].createTime.substring(0, 10) + " " + res.data[i].createTime.substring(11, 19),
                                idlePrice: res.data[i].orderPrice,
                                orderStatus: res.data[i].orderStatus
                            });
                        }
                        resolve();
                    } else {
                        reject(res);
                    }
                }).catch(error => {
                    reject(error);
                });
            });
        },

        getIdleItemData() {
            return new Promise((resolve, reject) => {
                const userId = this.userInfo.id; // 从userInfo获取用户ID
                this.$api.getAllIdleItem({ userId }).then(res => {
                    console.log(res);
                    if (res.status_code === 1) {
                        this.dataList[0] = []; // 清空刚刚发布数据
                        this.dataList[1] = []; // 清空已经下架数据
                        for (let i = 0; i < res.data.length; i++) {
                            res.data[i].timeStr = res.data[i].releaseTime.substring(0, 10) + " " + res.data[i].releaseTime.substring(11, 19);
                            let pictureList = JSON.parse(res.data[i].pictureList);
                            res.data[i].imgUrl = pictureList.length > 0 ? pictureList[0] : '';
                            if (res.data[i].idleStatus === 1) {
                                this.dataList[0].push(res.data[i]);
                            } else if (res.data[i].idleStatus === 2) {
                                this.dataList[1].push(res.data[i]);
                            }
                        }
                        this.isIdleLoaded = true; // 标记闲置物品数据已加载
                        resolve();
                    } else {
                        reject(res);
                    }
                }).catch(error => {
                    reject(error);
                });
            });
        },

        getAddressData() {
            this.$api.getAddress().then(res => {
                if (res.status_code === 1) {
                    let data = res.data;
                    for (let i = 0; i < data.length; i++) {
                        data[i].detailAddressText = data[i].provinceName + data[i].cityName + data[i].regionName + data[i].detailAddress;
                        data[i].defaultAddress = data[i].defaultFlag ? '默认地址' : '设为默认';
                    }
                    console.log(data);
                    this.addressData = data;
                } else {
                    console.error('获取收货地址数据失败:', res);
                    // 可以添加提示信息告知用户获取数据失败
                }
            }).catch(error => {
                console.error('获取收货地址数据请求错误:', error);
                // 可以添加提示信息告知用户网络错误
            });
        },

        handleClick(tab, event) {
            console.log(this.activeName);
            // 根据当前激活的标签页加载对应数据
            if (this.activeName === '1' || this.activeName === '2') {
                this.getIdleItemData();
            } else if (this.activeName === '3') {
                this.getMyFavorite(); // 加载购物车数据
            }
        },

        saveUserNickname() {
            this.notUserNicknameEdit = true;
            // 构造请求体，确保字段与后端User实体匹配
            const userData = {
                id: this.userInfo.id, // 需确保userInfo包含用户ID（从全局数据或接口返回中获取）
                nickname: this.userInfo.nickname,
                sex: this.userInfo.sex,
                birth: this.userInfo.birth, // 需为Date类型或符合数据库格式的字符串（如'yyyy-MM-dd'）
                avatar: this.userInfo.avatar // 头像URL
            };
            this.$api.updateUserPublicInfo(userData).then(res => {
                console.log(res);
                this.$globalData.userInfo = { ...this.$globalData.userInfo, ...userData }; // 更新全局用户信息
            });
        },

        savePassword() {
            if (!this.userPassword1 || !this.userPassword2) {
                this.$message.error('密码为空！');
            } else if (this.userPassword2 === this.userPassword3) {
                this.$api.updatePassword({
                    oldPassword: this.userPassword1,
                    newPassword: this.userPassword2
                }).then(res => {
                    if (res.status_code === 1) {
                        this.userPasswordEdit = false;
                        this.$message({
                            message: '修改成功！',
                            type: 'success'
                        });
                        this.userPassword1 = '';
                        this.userPassword2 = '';
                        this.userPassword3 = '';
                    } else {
                        this.$message.error('旧密码错误，修改失败！');
                    }
                })
            } else {
                this.$message.error('两次输入的密码不一致！');
            }
        },

        finishEdit() {
            this.notUserNicknameEdit = true;
            this.userInfoDialogVisible = false;
            this.userPasswordEdit = false;
        },

        handleAddressChange(value) {
            console.log(value);
            this.addressInfo.provinceName = value[0];
            this.addressInfo.cityName = value[1];
            this.addressInfo.regionName = value[2];
        },

        handleEdit(index, row) {
            console.log(index, row);
            this.addressInfo = JSON.parse(JSON.stringify(row));
            this.selectedOptions = ['', '', ''];
            this.selectedOptions[0] = row.provinceName;
            this.selectedOptions[1] = row.cityName;
            this.selectedOptions[2] = row.regionName;
        },

        handleDelete(index, row) {
            console.log(index, row);
            this.$confirm('是否确定删除该地址?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$api.deleteAddress(row).then(res => {
                    if (res.status_code === 1) {
                        this.$message({
                            message: '删除成功！',
                            type: 'success'
                        });
                        this.addressData.splice(index, 1);
                        if (row.defaultFlag && this.addressData.length > 0) {
                            this.addressData[0].defaultFlag = true;
                            this.addressData[0].defaultAddress = '默认地址';
                            this.update({
                                id: this.addressData[0].id,
                                defaultFlag: true
                            });
                        }
                    } else {
                        this.$message.error('系统异常，删除失败！')
                    }
                }).catch(() => {
                    this.$message.error('网络异常！')
                });
            }).catch(() => {
            });
        },

        handleSetDefault(index, row) {
            console.log(index, row);
            row.defaultFlag = true;
            this.update(row);
        },

        toDetails(activeName, item) {
            if (activeName === '4' || activeName === '5') {
                this.$router.push({ path: '/order', query: { id: item.id } });
            } else {
                this.$router.push({ path: '/details', query: { id: item.id } });
            }
        },

        handle(activeName, item, index) {
            console.log(activeName, item, index);
            this.$confirm('是否确认？', '提示', {
                confirmButtonText: activeName === '2' ? '上架' : '确认',
                cancelButtonText: '取消',
                type: activeName === '2' ? 'primary' : 'warning'
            }).then(() => {
                if (activeName === '1') {
                    // 下架操作
                    this.$api.updateIdleItem({
                        id: item.id,
                        idleStatus: 2
                    }).then(res => {
                        if (res.status_code === 1) {
                            // 从刚刚发布列表移除
                            this.dataList[0].splice(index, 1);
                            // 更新商品状态
                            item.idleStatus = 2;
                            // 添加到已经下架列表
                            this.dataList[1].unshift(item);
                            // 从购物车中移除（如果存在）
                            this.removeFromCart(item.id);
                            // 强制刷新数据（确保响应式）
                            this.dataList[0] = [...this.dataList[0]];
                            this.dataList[1] = [...this.dataList[1]];
                            // 自动切换到已经下架标签页
                            this.activeName = '2';
                            this.$message.success('商品已下架');
                        } else {
                            this.$message.error(res.msg);
                        }
                    });
                } else if (activeName === '2') {
                    if (item.idleStatus === 2) {
                        // 上架操作
                        this.$api.updateIdleItem({
                            id: item.id,
                            idleStatus: 1
                        }).then(res => {
                            if (res.status_code === 1) {
                                // 从已下架列表移除
                                this.dataList[1].splice(index, 1);
                                // 更新商品状态
                                item.idleStatus = 1;
                                // 添加到刚刚发布列表
                                this.dataList[0].unshift(item);
                                // 强制刷新数据（确保响应式）
                                this.dataList[0] = [...this.dataList[0]];
                                // 主动刷新数据
                                this.refreshIdleData();
                                // 如果当前在已下架标签页，自动切换到刚刚发布
                                if (this.activeName === '2') {
                                    this.activeName = '1';
                                }
                                this.$message.success('商品已上架');
                            } else {
                                this.$message.error(res.msg);
                            }
                        });
                    } else {
                        // 删除操作
                        this.$api.updateIdleItem({
                            id: item.id,
                            idleStatus: 0
                        }).then(res => {
                            if (res.status_code === 1) {
                                this.dataList[1].splice(index, 1);
                                this.$message.success('商品已删除');
                            } else {
                                this.$message.error(res.msg);
                            }
                        });
                    }
                } else if (activeName === '3') {
                    this.$api.deleteFavorite({
                        id: item.favoriteId
                    }).then(res => {
                        if (res.status_code === 1) {
                            this.$message({
                                message: '已取消购物车！',
                                type: 'success'
                            });
                            this.dataList[2].splice(index, 1);
                        } else {
                            this.$message.error(res.msg);
                        }
                    }).catch(e => {
                    })
                }
            }).catch(() => {
            });
        },

        // 编辑闲置商品
        handleEditItem(item) {
            // 复制商品信息到编辑信息中
            this.editIdleItemInfo = { ...item };
            // 解析图片列表（确保数据格式正确）
            this.editImgList = item.pictureList ? JSON.parse(item.pictureList) : [];
            this.selectedOptions = [item.idlePlace];
            this.editDialogVisible = true;
        },
        // 在methods中找到并修改saveEdit方法
        saveEdit() {
            // 验证必填字段
            if (!this.editIdleItemInfo.idleName) {
                this.$message.error('请填写闲置名称');
                return;
            }
            if (!this.editIdleItemInfo.idleDetails) {
                this.$message.error('请填写详细介绍');
                return;
            }
            if (this.editImgList.length === 0) {
                this.$message.error('请上传至少一张图片');
                return;
            }

            // 构建请求数据（确保图片列表为JSON字符串）
            this.editIdleItemInfo.pictureList = JSON.stringify(this.editImgList);

            // 保存编辑信息
            this.$api.updateIdleItem(this.editIdleItemInfo)
                .then(res => {
                    if (res.status_code === 1) {
                        this.$message({
                            message: '编辑成功！',
                            type: 'success'
                        });

                        // 关闭编辑对话框
                        this.editDialogVisible = false;

                        // 更新本地数据或重新加载数据
                        this.getIdleItemData(); // 刷新数据列表

                        // 如果需要，更新购物车中的数据
                        if (this.activeName === '3') {
                            this.getMyFavorite();
                        }
                    } else {
                        this.$message.error('编辑失败：' + (res.msg || '服务器错误'));
                    }
                })
                .catch(error => {
                    console.error('保存编辑信息时出错', error);
                    this.$message.error('网络错误，请稍后再试');
                });
        },

// 添加新方法：直接更新本地数据
        updateLocalData(updatedItem) {
            // 查找并更新刚刚发布列表中的数据
            const index = this.dataList[0].findIndex(item => item.id === updatedItem.id);
            if (index !== -1) {
                // 创建一个新对象，确保所有属性都被更新
                const newItem = {
                    ...this.dataList[0][index],
                    ...updatedItem,
                    imgUrl: this.editImgList.length > 0 ? this.editImgList[0] : '',
                    timeStr: this.formatTime(new Date())
                };

                // 使用Vue.set或数组splice方法确保响应式更新
                this.$set(this.dataList[0], index, newItem);

                // 可选：如果需要更新购物车中的数据
                const cartIndex = this.dataList[2].findIndex(item => item.id === updatedItem.id);
                if (cartIndex !== -1) {
                    this.$set(this.dataList[2], cartIndex, {
                        ...this.dataList[2][cartIndex],
                        idleName: updatedItem.idleName,
                        idleDetails: updatedItem.idleDetails,
                        idlePrice: updatedItem.idlePrice,
                        imgUrl: this.editImgList.length > 0 ? this.editImgList[0] : ''
                    });
                }
            }
        },

// 添加格式化时间的辅助方法
        formatTime(date) {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            const seconds = String(date.getSeconds()).padStart(2, '0');
            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
        },

// 修改closeEditDialog方法，确保清空数据
        // 关闭编辑对话框时重置数据
        closeEditDialog() {
            this.editIdleItemInfo = {
                idleName: '',
                idleDetails: '',
                pictureList: '',
                idlePrice: 0,
                idlePlace: '',
                idleLabel: ''
            };
            this.editImgList = [];  // 确保清空图片列表
            this.selectedOptions = [];
            this.showFileList = true;  // 重置文件列表显示状态
        },

        // 从购物车中删除商品
        removeFromCart(itemId) {
            const cartIndex = this.dataList[2].findIndex(item => item.id === itemId);
            if (cartIndex !== -1) {
                this.dataList[2].splice(cartIndex, 1);
                this.$message.info('已从购物车中移除该商品');
            }
        },

        // 处理闲置物品图片上传
        editFileHandleSuccess(response, file, fileList) {
            console.log("闲置物品图片上传成功:", response, file, fileList);
            // 确保只保留上传成功的图片URL
            const imgUrl = response.data;

            // 如果是新上传的图片，添加到列表中
            if (!this.editImgList.includes(imgUrl)) {
                this.editImgList.push(imgUrl);
            }

            // 限制图片数量不超过10张
            if (this.editImgList.length > 10) {
                this.editImgList.shift(); // 移除最早添加的图片
            }
        },

// 处理头像上传（保持原有的逻辑）
        fileHandleSuccess(response, file, fileList) {
            console.log("头像上传成功:", response, file, fileList);
            let imgUrl = response.data;
            this.imgFileList = [];
            this.$api.updateUserPublicInfo({
                avatar: imgUrl
            }).then(res => {
                console.log(res);
                this.userInfo.avatar = imgUrl;
                this.$globalData.userInfo.avatar = imgUrl;
            })
        },
        // 处理图片删除
        editFileHandleRemove(file, fileList) {
            console.log("删除图片:", file, fileList);
            const imgUrl = file.response.data;
            const index = this.editImgList.findIndex(img => img === imgUrl);

            if (index !== -1) {
                // 使用splice确保响应式更新
                this.editImgList.splice(index, 1);
            }
        },

        update(data) {
            this.$api.updateAddress(data).then(res => {
                if (res.status_code === 1) {
                    this.getAddressData();
                    this.$message({
                        message: '修改成功！',
                        type: 'success'
                    });
                } else {
                    this.$message.error('系统异常，修改失败！')
                }
            }).catch(() => {
                this.$message.error('网络异常！')
            })
        },

        saveAddress() {
            if (this.addressInfo.id) {
                console.log('update:', this.addressInfo);
                this.update(this.addressInfo);
                this.addressInfo = {
                    consigneeName: '',
                    consigneePhone: '',
                    provinceName: '',
                    cityName: '',
                    regionName: '',
                    detailAddress: '',
                    defaultFlag: false
                };
                this.selectedOptions = [];
            } else {
                if (this.addressData.length >= 5) {
                    this.$message.error('已达到最大地址数量！')
                } else {
                    console.log(this.addressInfo);
                    this.$api.addAddress(this.addressInfo).then(res => {
                        if (res.status_code === 1) {
                            this.getAddressData();
                            this.$message({
                                message: '新增成功！',
                                type: 'success'
                            });
                            this.selectedOptions = [];
                            this.addressInfo = {
                                consigneeName: '',
                                consigneePhone: '',
                                provinceName: '',
                                cityName: '',
                                regionName: '',
                                detailAddress: '',
                                defaultFlag: false
                            };
                        } else {
                            this.$message.error('系统异常，新增失败！')
                        }
                    }).catch(e => {
                        this.$message.error('网络异常！')
                    })
                }
            }
        }
    },
    // 添加watch选项，与data、methods同级
    watch: {
        eidtAddress: {
            handler(newValue) {
                if (newValue) {
                    this.getAddressData();
                }
            },
            immediate: true
        }
    }
}


</script>

<style scoped>
/* 样式部分保持不变 */
.user-info-container {
    width: 100%;
    height: 200px;
    border-bottom: 15px solid #f6f6f6;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.user-info-details {
    display: flex;
    height: 140px;
    align-items: center;
    margin: 20px 40px;
}

.user-info-details-text {
    margin-left: 20px;
}

.user-info-details-text-nickname {
    font-size: 26px;
    font-weight: 600;
    margin: 10px 0;
}

.user-info-details-text-time {
    font-size: 14px;
    margin-bottom: 10px;
}

.user-info-splace {
    margin-right: 90px;
}

.idle-container {
    padding: 0 20px;
}

.idle-container-list {
    min-height: 55vh;
}

.idle-container-list-item {
    border-bottom: 1px solid #eeeeee;
    cursor: pointer;
}

.idle-container-list-item:last-child {
    border-bottom: none;
}

.idle-container-list-item-detile {
    height: 120px;
    display: flex;
    align-items: center;
}

.idle-container-list-item-text {
    margin-left: 10px;
    height: 100px;
    max-width: 800px;
}

.idle-container-list-title {
    font-weight: 600;
    font-size: 18px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.idle-container-list-idle-details {
    font-size: 14px;
    color: #555555;
    padding-top: 5px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.idle-container-list-idle-time {
    font-size: 13px;
    padding-top: 5px;
}

.idle-prive {
    font-size: 15px;
    padding-top: 5px;
    color: red;
}

.edit-tip {
    font-size: 14px;
    margin: 10px 5px;
}

.address-container {
    padding: 10px 20px;
}

.address-container-back {
    margin-bottom: 10px;
}

.address-container-add-title {
    font-size: 15px;
    color: #409EFF;
    padding: 10px;
}

.address-container-add-item {
    margin-bottom: 20px;
}

.demonstration {
    color: #666666;
    font-size: 14px;
    padding: 10px;
}

.address-container-add {
    padding: 0 200px;
}

.address-container-list {
    padding: 30px 100px;
}

.idle-item-foot {
    width: 800px;
    display: flex;
    justify-content: space-between;
}

/* 新增头像样式 */
.avatar-edit-container {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
}

.avatar-preview {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    cursor: pointer;
}

.avatar-placeholder {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background-color: #f5f7fa;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #909399;
    cursor: pointer;
}

.avatar-placeholder i {
    font-size: 30px;
    margin-bottom: 5px;
}
/* 添加新的图片网格布局样式 */
.edit-image-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
    gap: 10px;
    margin-top: 10px;
}

.edit-image-item {
    width: 100%;
    height: 120px;
    border-radius: 4px;
    overflow: hidden;
}

/* 可以根据需要调整图片的尺寸 */
.edit-image-item img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
/* 结算栏样式 - 已修改为非固定定位 */
.cart-checkout-bar {
    height: 50px;
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
    margin-top: 10px;
    margin-bottom: 20px;
}

.cart-select-all {
    display: flex;
    align-items: center;
}

.cart-total-price {
    font-size: 16px;
    font-weight: bold;
}

.total-amount {
    color: #f56c6c;
    margin-left: 5px;
}
/* 上传区域样式优化 */
.release-idle-container-picture {
    min-height: 300px;
    margin-top: 20px;
}

/* 上传按钮样式 */
.el-upload {
    width: 100%;
    max-width: 350px;
    height: 120px;
    border: 2px dashed #dcdfe6;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: #fafafa;
    transition: all 0.3s ease;
    margin-bottom: 20px;
}

.el-upload:hover {
    border-color: #409EFF;
    background-color: #f5f7fa;
}

/* 图片网格布局 */
.edit-image-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
    gap: 15px;
}

/* 单个图片项样式 */
.image-item {
    position: relative;
    width: 100%;
    height: 120px;
    border-radius: 4px;
    overflow: hidden;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.image-wrapper {
    width: 100%;
    height: 100%;
    position: relative;
}

.uploaded-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

/* 图片悬停效果 */
.image-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    opacity: 0;
    transition: opacity 0.2s ease;
    cursor: pointer;
}

.image-item:hover .image-overlay {
    opacity: 1;
}

.image-overlay i {
    color: white;
    font-size: 20px;
}

/* 图片名称样式 */
.image-name {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.6);
    color: white;
    padding: 3px 6px;
    font-size: 12px;
    text-align: center;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
/* 防止其他元素覆盖复选框 */
.idle-container-list-item-detile {
    position: relative;
    z-index: 1;
}
</style>
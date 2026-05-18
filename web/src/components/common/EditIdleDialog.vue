<template>
    <el-dialog
        v-model="dialogVisible"
        title="编辑闲置物品"
        width="60%"
        center
    >
        <div class="release-idle-container-form">
            <el-input
                placeholder="请输入闲置/公告标题"
                v-model="idleItemInfo.idleName"
                maxlength="30"
                show-word-limit
            >
            </el-input>
            <el-input
                class="release-idle-detiles-text"
                type="textarea"
                autosize
                placeholder="请输入闲置/公告的详细介绍..."
                v-model="idleItemInfo.idleDetails"
                maxlength="1000"
                show-word-limit
            >
            </el-input>
            <div class="release-idle-place">
                <div class="release-tip">您的位置</div>
                <el-cascader
                    :options="options"
                    v-model="selectedOptions"
                    @change="handleChange"
                    :separator="' '"
                    style="width: 90%;"
                >
                </el-cascader>
            </div>
            <div style="display: flex; justify-content: space-between;">
                <div>
                    <div class="release-tip">闲置类别</div>
                    <el-select v-model="idleItemInfo.idleLabel" placeholder="请选择类别">
                        <el-option
                            v-for="item in options2"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        >
                        </el-option>
                    </el-select>
                </div>
                <div v-show="idleItemInfo.idleLabel !== 5" style="width: 300px;">
                    <el-input-number
                        v-model="idleItemInfo.idlePrice"
                        :precision="2"
                        :step="10"
                        :max="10000000"
                    >
                        <div slot="prepend">价格</div>
                    </el-input-number>
                </div>
            </div>
            <div class="release-idle-container-picture">
                <div class="release-idle-container-picture-title">上传闲置照片</div>
                <el-upload
                    action="http://localhost:8888/file/"
                    :on-preview="fileHandlePreview"
                    :on-remove="fileHandleRemove"
                    :on-success="fileHandleSuccess"
                    :show-file-list="showFileList"
                    :limit="10"
                    :on-exceed="handleExceed"
                    accept="image/*"
                    drag
                    multiple
                >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
                </el-upload>
                <div class="picture-list">
                    <el-image
                        style="width: 600px;margin-bottom: 2px;"
                        fit="contain"
                        v-for="(img,index) in imgList"
                        :key="index"
                        :src="img"
                        :preview-src-list="imgList"
                    ></el-image>
                </div>
                <el-dialog :visible.sync="imgDialogVisible">
                    <img width="100%" :src="dialogImageUrl" alt="">
                </el-dialog>
            </div>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="updateIdleItem">确认修改</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import options from './country-data.js'

export default {
    name: "EditIdleDialog",
    props: {
        visible: {
            type: Boolean,
            default: false
        },
        idleItem: {
            type: Object,
            default: () => ({})
        }
    },
    data() {
        return {
            imgDialogVisible: false,
            dialogImageUrl: '',
            showFileList: true,
            options: options,
            selectedOptions: [],
            options2: [
                { value: 1, label: '文学艺术' },
                { value: 2, label: '人文社科' },
                { value: 3, label: '经济管理' },
                { value: 4, label: '生活休闲' },
                { value: 5, label: '公告' }
            ],
            imgList: [],
            idleItemInfo: {
                idleName: '',
                idleDetails: '',
                pictureList: '',
                idlePrice: 0,
                idlePlace: '',
                idleLabel: ''
            }
        };
    },
    watch: {
        // 监听父组件传递的idleItem变化，更新表单数据
        idleItem: {
            handler(newVal) {
                if (newVal) {
                    this.idleItemInfo = {
                        ...newVal,
                        idlePrice: parseFloat(newVal.idlePrice) || 0,
                        idleLabel: parseInt(newVal.idleLabel) || ''
                    };

                    // 处理图片列表
                    if (newVal.pictureList) {
                        try {
                            this.imgList = JSON.parse(newVal.pictureList);
                        } catch (e) {
                            this.imgList = [newVal.pictureList]; // 兼容单图情况
                        }
                    }

                    // 处理位置信息
                    if (newVal.idlePlace) {
                        // 假设idlePlace格式为"城市名"，需要从options中找到对应的级联选择
                        this.selectedOptions = this.findCascaderOptions(newVal.idlePlace);
                    }
                }
            },
            immediate: true
        },
        visible(val) {
            this.dialogVisible = val;
        }
    },
    methods: {
        // 查找级联选择器选项
        findCascaderOptions(cityName) {
            if (!cityName) return [];
            // 简化实现，实际应根据完整地址数据查找
            return this.options.find(province =>
                province.children.find(city => city.label === cityName)
            )?.children.find(city => city.label === cityName)
                ? [cityName] : [];
        },
        handleChange(value) {
            this.idleItemInfo.idlePlace = value[1];
        },
        fileHandleRemove(file, fileList) {
            for (let i = 0; i < this.imgList.length; i++) {
                if (this.imgList[i] === file.response.data) {
                    this.imgList.splice(i, 1);
                }
            }
        },
        fileHandlePreview(file) {
            this.dialogImageUrl = file.response.data;
            this.imgDialogVisible = true;
        },
        fileHandleSuccess(response, file, fileList) {
            this.imgList.push(response.data);
        },
        // 提交编辑后的闲置物品
        updateIdleItem() {
            this.idleItemInfo.pictureList = JSON.stringify(this.imgList);

            // 验证表单
            if (
                !this.idleItemInfo.idleName ||
                !this.idleItemInfo.idleDetails ||
                !this.idleItemInfo.idlePlace ||
                !this.idleItemInfo.idleLabel ||
                (this.idleItemInfo.idleLabel !== 5 && !this.idleItemInfo.idlePrice)
            ) {
                this.$message.error('请填写完整信息！');
                return;
            }

            // 发送更新请求 - 修改为使用您提供的API
            this.$api.updateIdleItem({
                id: this.idleItem.id, // 假设idleItem包含id
                ...this.idleItemInfo
            }).then(res => {
                if (res.status_code === 1) {
                    this.$message({
                        message: '修改成功！',
                        type: 'success'
                    });
                    this.dialogVisible = false;
                    this.$emit('updateSuccess'); // 通知父组件更新数据
                } else {
                    this.$message.error('修改失败！' + res.msg);
                }
            }).catch(e => {
                this.$message.error('网络错误，请重试');
            });
        },
        handleExceed(files, fileList) {
            this.$message.warning(`限制10张图片，本次选择了 ${files.length} 张图，共选择了 ${files.length + fileList.length} 张图`);
        }
    }
}
</script>

<style scoped>
/* 样式与release.vue相同，可复制过来 */
.release-idle-detiles-text {
    margin: 20px 0;
}
.release-idle-place{
    margin-bottom: 15px;
}
.release-tip{
    color: #555555;
    float: left;
    padding-right: 5px;
    height: 36px;
    line-height: 36px;
    font-size: 14px;
}
.release-idle-container-picture{
    margin: 20px 0;
}
.release-idle-container-picture-title{
    margin: 10px 0;
    color: #555555;
    font-size: 14px;
}
.picture-list {
    margin: 20px 0;
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>
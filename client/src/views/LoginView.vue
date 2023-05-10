<script setup lang="ts">
import {reactive} from "vue";

const loginInfo = reactive({
    username: '',
    password: ''
})
</script>

<template>
    <el-form class="password-form" :model="loginInfo" label-width="100px">
        <el-form-item label="用户名">
            <el-input v-model="loginInfo.username"/>
        </el-form-item>
        <el-form-item label="密码">
            <el-input show-password v-model="loginInfo.password"/>
        </el-form-item>
    </el-form>
</template>

<style scoped>
.password-form {
    width: 500px;
}
</style>
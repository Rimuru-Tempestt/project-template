<script setup lang="ts">
import {reactive} from "vue";
import axiosInstance from "@/assets/ts/axiosInInstance";

const loginInfo = reactive({
    username: '',
    password: ''
})

function login() {
    axiosInstance.post('/api/login', loginInfo).then(response => {
        console.log(response);
    })
}
</script>

<template>
    <el-form class="password-form" :model="loginInfo" label-width="100px">
        <el-form-item label="用户名">
            <el-input v-model="loginInfo.username"/>
        </el-form-item>
        <el-form-item label="密码">
            <el-input show-password v-model="loginInfo.password"/>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="login">登陆</el-button>
        </el-form-item>
    </el-form>
</template>

<style scoped>
.password-form {
    width: 500px;
}
</style>
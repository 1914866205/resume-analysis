<template>
  <!-- <div class="main"> -->
  <div>
    <!-- <div class="logo"><img src="../assets/imgs/Logo.png" width="180px" /></div> -->
    <!-- <el-button class="btn" style="position: fixed; right: 5%; bottom: 20%">
      <router-link style="text-decoration: none; color: #000" to="/resume"
        >简历解析服务</router-link
      >
    </el-button> -->
    <div class="font"><span>合同解析服务</span></div>
    <div class="tip">
      <!-- <pre> -->
          使用说明：把需要解析的合同pdf统一放在一个叫 root 的文件夹，再一起压缩成 root.zip 上传。
          功能介绍：可根据提供的合同，整理姓名、合同时间并命名。
        <!-- </pre
      > -->
    </div>

    <el-upload
      class="upload"
      action="http://47.101.190.57:8090/contract/import"
      :on-preview="handlePreview"
      ref="upload"
      accept=".zip"
      :on-remove="handleRemove"
      :before-remove="beforeRemove"
      :limit="1"
      :on-exceed="handleExceed"
      :on-success="handleSuccess"
      :file-list="fileList"
      :auto-upload="false"
    >
      <el-button class="btn" size="small">选择文件</el-button>
      <div slot="tip" class="el-upload__tip" style="color: white">
        只能上传 .zip 文件
      </div>
    </el-upload>
    <el-button class="btn" size="small" type="primary" @click="upload"
      >点击上传</el-button
    >
    <el-button
      class="btn"
      size="small"
      type="primary"
      @click="download"
      :disabled="path === 0 || path == 1"
      >{{ path === 1 ? "正在处理" : "下载处理结果" }}</el-button
    >

    <!-- <div class="footer">
      <hr />
      技术支持：nitaotao@zpmc.com
    </div> -->
    <el-dialog
      title="防刷校验"
      :visible.sync="dialogVisible"
      width="15%"
      :before-close="handleClose"
      :modal-append-to-body="false"
    >
      <el-input type="text" v-model="resCode" placeholder="请输入验证码" />
      <img class="code-image" :src="codeUrl" alt="验证码" @click="getCode" />
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="confirmUpload">确 定</el-button>
    </el-dialog>
  </div>
</template>
  
  <script>
import axios from "axios";
export default {
  name: "indelx-page",
  components: {},
  data() {
    return {
      fileList: [],
      path: 0,
      dialogVisible: false,
      code: "",
      codeUrl: "",
      resCode: "",
    };
  },
  computed: {},
  created() {
    this.getCode();
  },
  mounted() {},
  methods: {
    getCode() {
      axios
        .post("http://47.101.190.57:8090/code/getCode", {
          uuid: this.uuid,
        })
        .then((res) => {
          this.codeUrl = "data:image/gif;base64," + res.data.data.img;
          this.uuid = res.data.data.uuid;
        });
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        // eslint-disable-next-line no-unused-vars
        .then((_) => {
          done();
        })
        // eslint-disable-next-line no-unused-vars
        .catch((_) => {});
    },
    async download() {
      try {
        const response = await axios.get(
          "http://47.101.190.57:8090/contract/export/" + this.path,
          {
            responseType: "blob", // 设置响应数据类型为二进制
          }
        );

        if (response.status === 200) {
          const blob = new Blob([response.data], { type: "application/zip" });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = url;
          link.download = "analysis_contract_result.zip"; // 设置下载文件名
          link.click();
          window.URL.revokeObjectURL(url);
        } else {
          console.error("下载ZIP文件失败");
        }
      } catch (error) {
        console.error("下载ZIP文件失败", error);
      }
    },
    confirmUpload() {
      axios
        .post("http://47.101.190.57:8090/code/checkCode", {
          uuid: this.uuid,
          img: this.resCode,
        })
        .then((res) => {
          if (res.data.data == true) {
            //验证码验证通过
            this.$refs.upload.submit();
            this.path = 1;
            this.$message({
              message: "验证码通过，开始上传",
              type: "success",
            });
          } else {
            //验证码验证不通过
            this.$message({
              message: "验证码错误",
              type: "error",
            });
          }
        });
      this.resCode = "";
      this.dialogVisible = false;
    },
    async upload() {
      if (this.$refs.upload.uploadFiles.length > 0) {
        this.dialogVisible = true;
      } else {
        this.$message({
          message: "请先添加文件",
          type: "error",
        });
      }
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleSuccess(response) {
      this.path = response.message;
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
          files.length + fileList.length
        } 个文件`
      );
    },
    // eslint-disable-next-line no-unused-vars
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
  },
};
</script>
  
  <style scoped>
.main {
  background: url("https://img2.baidu.com/it/u=2931821434,3562319821&fm=253&fmt=auto&app=138&f=PNG?w=500&h=1003");
  background-repeat: no-repeat;
  background-size: 100% 100%;
  position: fixed;
  height: 100%;
  width: 100%;
  top: 0;
  left: 0;
}
.upload {
  margin-left: 40%;
  margin-top: 5%;
  width: 20%;
  color: white;
}
.btn {
  margin: 2%;
}
img {
  margin-top: 4%;
  background-repeat: no-repeat;
}
.font {
  color: #fff;
  font-size: 45px;
  font-weight: bold;
  height: 110px;
  line-height: 110px;
  overflow: hidden;
  margin: 20px auto 0 auto;
  margin-top: 100px;
  /* 字符间隔 */
  letter-spacing: 20px;
}
.footer {
  color: #fff;
  font-size: 14px;
  display: block;
  position: fixed;
  bottom: 5%;
  width: 100%;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  font-family: "微软雅黑", SYHT, "Microsoft Yahei", Arial, Helvetica, sans-serif;
}
.tip {
  color: white;
  position: fixed;
  width: 480px;
  left: 61%;
  text-align: left;
  bottom: 45%;
  font-size: 12px;
}
.code {
  display: inline-block;
}

.code-image {
  display: block;
  margin: 10px;
  width: 130px;
  height: 50px;
  line-height: 50px;
  margin-left: 25%;
  cursor: pointer;
}
</style>
  
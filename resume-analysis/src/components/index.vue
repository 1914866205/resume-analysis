!<template>
  <div class="main">
    <div>
        <Resume class="resume"/>
        <Contract class="contract"/>
    </div>
    <div class="footer">
      <hr />
      技术支持：nitaotao@zpmc.com
    </div>
</div>
</template>

<script>
import Resume from "../views/resume/index.vue";
import Contract from "../views/contract/index.vue";
export default {
  name: "IndexPage",
  components: {
    Resume,
    Contract,
  },
  data() {
    return {};
  },
  methods: {},
  computed: {},
  watch: {},
  mounted() {},
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
.resume{
    position: relative;
    float: left;
    width: 50%;
}
.contract{
    position: relative;
    float: right;
    width: 50%;
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
</style>
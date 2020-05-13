<template>
  <v-card>
      <v-flex xs12 sm10>
        <v-tree url="/item/category/list"

                :isEdit="isEdit"
                @handleAdd="handleAdd"
                @handleEdit="handleEdit"
                @handleDelete="handleDelete"
                @handleClick="handleClick"
        />
      </v-flex>
  </v-card>
</template>

<script>
  import {treeData} from "../../mockDB";

  export default {
    name: "category",
    data() {
      return {
        isEdit:true,
        treeData
      }
    },
    methods: {
      handleAdd(node) {
        this.$http.post(
          "/item/category",
          this.$qs.stringify(node)
          ).then(resp => { // 这里使用箭头函数
          this.$message.success("添加成功！");
        })
          .catch(() => {
            this.$message.error("添加失败！");
          });
        this.handleClick();
        console.log("add .... ");
        console.log(node);
      },
      handleEdit(id,name) {
        const node={
          id:id,
          name:name
        }
        // 根据品牌信息查询商品分类
        this.$http.put(
          "/item/category",
          this.$qs.stringify(node)
        ).then(resp => { // 这里使用箭头函数
          this.$message.success("修改成功！");
        })
          .catch(() => {
            this.$message.error("修改失败！");
          });
        console.log("edit... id: " + id + ", name: " + name)
      },
      handleDelete(id) {
        this.$http.delete("/item/category/cid/"+id)
          .then(resp => { // 这里使用箭头函数
          this.$message.success("删除成功！");
        })
          .catch(() => {
            this.$message.error("删除失败！");
          });
      },
      handleClick(node) {
        console.log(node)
      }
    }
  };
</script>

<style scoped>

</style>

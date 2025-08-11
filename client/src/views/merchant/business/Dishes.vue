<template>
  <div class="dish-management-container">
    <el-page-header content="菜品管理" style="margin-bottom: 20px;"></el-page-header>
    
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabChange">
      <el-tab-pane label="菜品" name="dish"></el-tab-pane>
      <el-tab-pane label="分类" name="category"></el-tab-pane>
      <el-tab-pane label="菜系" name="styleOfCooking"></el-tab-pane>
      <el-tab-pane label="口味" name="flavor"></el-tab-pane>
      <el-tab-pane label="规格" name="spec"></el-tab-pane>
      <el-tab-pane label="原料类别" name="ingredientType"></el-tab-pane>
      <el-tab-pane label="原料" name="ingredient"></el-tab-pane>
    </el-tabs>
    
    <!-- 子页面容器 -->
    <div class="content-container">
      <DishList v-if="activeTab === 'dish'" />
      <CategoryList v-if="activeTab === 'category'" />
      <FlavorList v-if="activeTab === 'flavor'" />
      <SpecificationList v-if="activeTab === 'spec'" />
      <IngredientTypeList v-if="activeTab === 'ingredientType'" />
      <IngredientList v-if="activeTab === 'ingredient'" />
      <StyleOfCookingList v-if="activeTab === 'styleOfCooking'" />
    </div>
  </div>
</template>

<script>
import DishList from './dish-management/DishList.vue' // 菜品列表
import CategoryList from './dish-management/CategoryList.vue' // 分类列表
import FlavorList from './dish-management/FlavorList.vue' // 口味列表
import SpecificationList from './dish-management/SpecificationList.vue' // 规格列表
import IngredientTypeList from './dish-management/IngredientTypeList.vue' // 原料类别列表
import IngredientList from './dish-management/IngredientList.vue' // 原料列表
import StyleOfCookingList from './dish-management/styleOfCookingList.vue' // 菜系列表


export default {
  name: 'DishManagement',
  components: {
    DishList,
    CategoryList,
    FlavorList,
    SpecificationList,
    IngredientTypeList,
    IngredientList,
    StyleOfCookingList
  },
  data() {
    return {
      activeTab: 'dish' // 默认显示菜品页面
    }
  },
  methods: {
    handleTabChange(tab) {
      console.log('切换到:', tab.name)
    }
  }
}
</script>

<style scoped>
.dish-management-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 40px);
}

.content-container {
  margin-top: 20px;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}
</style>

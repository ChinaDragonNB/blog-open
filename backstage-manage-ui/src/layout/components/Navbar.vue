<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar"/>
    <breadcrumb class="breadcrumb-container"/>
    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="userInfo.loginImg" class="user-avatar"/>
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown" style="margin-top: -10px">
          <el-dropdown-item align="center">
            <svg-icon icon-class="navbar-user"></svg-icon>
            <span style="font-size: 13px;font-weight:bold;">{{ userInfo.nickName }}</span>
          </el-dropdown-item>
          <a target="_blank" :href="userInfo.blogHome" align="center">
            <el-dropdown-item>
              <svg-icon icon-class="navbar-blog"/>
              <span style="font-size: 13px;">My Blog</span>
            </el-dropdown-item>
          </a>
          <el-dropdown-item align="center">
            <svg-icon icon-class="navbar-singout"/>
            <span @click="logout" style="font-size: 13px;">Sign out</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div class="right-menu" style="margin-right:30px">
          <span style="font-size:14px;font-weight:bold;">
            <svg-icon icon-class="navbar-position"/>
            {{ userInfo.position }}
          </span>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import { getUserInfoApi, logoutApi } from '@/api/auth'
// Token工具
import { removeToken } from '@/utils/auth'

export default {
  data() {
    return {
      userInfo: {
        loginImg: '',
        nickName: '',
        position: '',
        blogHome: ''
      },
      notifyPromise: Promise.resolve()
    }
  },
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters(['sidebar', 'avatar'])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    logout() {
      logoutApi().then(res => {
        if (res.data.resultCode == 1) {
          removeToken()
          localStorage.removeItem('website')
          this.$router.push('/login')
        }
      })
    }
  },
  created() {
    getUserInfoApi().then(res => {
      if (res.data.resultCode == 1) {
        this.userInfo = res.data.resultData
        localStorage.setItem('website', this.blogHome)
      } else if (res.data.resultCode == -1) {
        this.$message.error(res.data.resultMessage)
      }
    })
  }
}
</script>


<style lang="scss" scoped>
.item {
  position: relative;
  top: -10px;
  right: 10px;
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>

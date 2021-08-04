<template>
  <div class="app-container">
    <div>
      <el-button plain icon="el-icon-arrow-left" size="small" @click="clickBack" style="float: left"></el-button>
      <el-button icon="el-icon-refresh" plain size="small" @click="clickRefresh"></el-button>
      <el-upload :http-request="uploadFile" action="" :show-file-list="false"
                 style="display: inline">
        <el-button type="primary" size="small">上传文件<i class="el-icon-upload el-icon--right"></i></el-button>
      </el-upload>
      <el-button size="small" @click="clickAddFolder">新建目录<i class="el-icon-folder-add el-icon--right"></i>
      </el-button>
      <el-input placeholder="请输入文件名前缀匹配" size="small" v-model="keyword" style="width: 11%;float: right">
        <el-button slot="append" icon="el-icon-search"></el-button>
      </el-input>
      <!--            <el-button @click="clickSearch" icon="el-icon-search" size="small">查询</el-button>-->
    </div>
    <br>

    <el-table v-loading="listLoading" :data="objectList" element-loading-text="加载中..." highlight-current-row>
      <el-table-column type="index" width="80" align="center" label="序号"/>
      <el-table-column label="文件名" align="center">
        <template slot-scope="scope">
          <el-link type="primary" target="_blank" :underline="false" @click="clickName(scope.row)">
            <svg-icon :icon-class="scope.row.iconType"></svg-icon>
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="文件大小" align="center" prop="formattedSize"></el-table-column>
      <el-table-column label="存储类型" align="center" prop="storageType"></el-table-column>
      <el-table-column label="最后更新" align="center" prop="lastModified">
      </el-table-column>
      <el-table-column class-name="status-col" label="操作" align="center">
        <template slot-scope="scope">
          <!--                    <el-button type="primary" size="mini">编辑</el-button>-->
          <el-button type="danger" size="mini" v-show="!scope.row.isDir"
                     @click="clickDel(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-drawer :visible.sync="drawer" style="top: 50px;" :size="'640px'" :wrapperClosable="false">
      <header class="dialog-hd"><span class="dialog-hd-title">详情</span></header>
      <div class="dialog-bd" data-spm-anchor-id="5176.8466032.0.i4.120614500AbAnl">
        <div class="oss-op-file-preview">
          <div class="preview-con">
            <div class="the-previewer-wrapper"><img
                :src="detailed.url"
                alt="文件无法预览。"></div>
          </div>
        </div>
        <el-form ref="form" :model="detailed" label-width="100px" size="small">
          <el-form-item label="文件名">
            <span style="margin-left: 20px;">{{ detailed.name }}</span>
            <span style="float: right;font-size: 12px;color:#66B1FF;cursor: pointer">复制</span>
          </el-form-item>
          <el-form-item label="文件类型">
            <span style="margin-left: 20px;">{{ detailed.fileType }}</span>
          </el-form-item>
          <el-form-item label="存储类型">
            <span style="margin-left: 20px;">{{ detailed.storageType }}</span>
          </el-form-item>
          <el-form-item label="URL">
            <el-input type="textarea" style="margin-left: 20px;" readonly
                      v-model="detailed.url"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
    <el-dialog title="新建目录" :visible.sync="folderDialogVisible" width="20%">
      <el-form>
        <el-form-item label="目录名">
          <el-input v-model="folderModel.folderName" autocomplete="off"></el-input>
          <div style="font-size: 12px">
            <span> 目录命名规范：</span>
            <ol>
              <li>不允许使用表情符，请使用符合要求的 UTF-8 字符；</li>
              <li> / 用于分割路径，可快速创建子目录，但不要以 / 或 \ 开头，不要出现连续的 /；</li>
              <li> 不允许出现名为 .. 的子目录；</li>
              <li> 总长度控制在 1~254 个字符。</li>
            </ol>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="folderDialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="addFolder" size="small">确 定</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>


import { addFolderApi, deleteObjectApi, getObjectListApi, uploadFileApi } from '@/api/alicloud'

export default {
  data() {
    return {
      bucketName: '',
      objectList: null,
      listLoading: false,
      keyword: null,
      path: null,
      // 上一级路径
      prePath: [],
      drawer: false,
      detailed: {},
      folderDialogVisible: false,
      folderModel: {
        folderName: ''
      }

    }
  },
  created() {
    const bucketName = this.$route.params.bucketName
    if (typeof (bucketName) != 'undefined') {
      this.bucketName = bucketName
    } else {
      this.$router.push('/aliCloud/bucket')
    }

    this.getData()
  },
  methods: {
    /**
     * 点击查询
     */
    clickSearch() {
      this.getData()
    },
    /**
     * 获取数据
     */
    getData() {
      this.listLoading = true
      const params = {
        bucketName: this.bucketName,
        path: this.path,
        keyword: this.keyword
      }
      getObjectListApi(params).then(res => {
        this.objectList = res.data.resultData
        this.listLoading = false
      })

    },
    /**
     * 点击文件名
     */
    clickName(obj) {
      // 点击的文件夹
      if (obj.isDir) {
        this.path = obj.path
        this.prePath.push(this.path)
        console.log(this.prePath)
        this.getData()
      } else {
        this.drawer = true
        this.detailed = obj
        this.detailed.fileType = obj.name.substr(obj.name.lastIndexOf('.') + 1)
      }

    },
    /**
     * 点击返回
     */
    clickBack: function() {
      this.prePath.pop()
      this.path = this.prePath[this.prePath.length - 1]
      this.getData()
    },
    /**
     * 点击刷新
     */
    clickRefresh() {
      this.getData()
    },
    /**
     * 点击添加目录
     */
    clickAddFolder() {
      this.folderDialogVisible = true
    },
    /**
     * 添加目录
     */
    addFolder() {
      this.folderDialogVisible = false
      const params = {
        path: this.path,
        bucketName: this.bucketName,
        folderName: this.folderModel.folderName
      }
      addFolderApi(params).then(res => {
        if (res.data.resultCode == 1) {
          this.$message.success(res.data.resultMessage)
          this.folderModel.folderName = ''
          this.getData()
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })

    },
    /**
     * 点击删除文件
     * @param index
     * @param row
     */
    clickDel(index, row) {
      this.$confirm('确定删除该文件吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const params = {
          bucketName: this.bucketName,
          key: row.path
        }
        deleteObjectApi(params).then(res => {
          if (res.data.resultCode == 1) {
            this.$message.success(res.data.resultMessage)
            this.objectList.splice(index, 1)
          } else if (res.data.resultCode == -1) {
            this.$message.error(res.data.resultMessage)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消删除'
        })
      })
    },
    /**
     *
     * 上传文件
     * @param $file
     */
    uploadFile($file) {
      const formData = new FormData()
      formData.append('path', this.path)
      formData.append('file', $file.file)
      uploadFileApi(formData).then(res => {
        if (res.data.resultCode == 1) {
          this.$message.success(res.data.resultMessage)
          this.getData()
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    }
  }
}
</script>
<style scoped>
.dialog-hd {
  padding: 0 64px 0 24px;
  border-bottom: 1px solid #ebebeb;
  height: 60px;
  line-height: 60px;
}

.dialog-hd .dialog-hd-title {
  font-size: 18px;
}

.dialog-bd {
  flex: 1;
  overflow: auto;
  padding: 25px;
}

.oss-op-file-preview {
  box-sizing: border-box;
}

.preview-con {
  position: relative;
  margin-bottom: 12px;
}

.preview-con .the-previewer-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 360px;
  background-color: #f5f5f5;
  color: #888;
}

.preview-con .the-previewer-wrapper img {
  max-width: 100%;
  max-height: 360px;
  display: block;
}

</style>

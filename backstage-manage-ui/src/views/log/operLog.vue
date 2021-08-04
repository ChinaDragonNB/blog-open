<template>
  <div class="block">
    <el-table :data="pageInfo.list" highlight-current-row>
      <el-table-column type="index" label="序号" width="80" align="center">
        <template slot-scope="scope"><span>{{ scope.$index + (pageInfo.pageNum - 1) * pageInfo.pageSize + 1 }} </span></template>
      </el-table-column>
      <el-table-column label="操作模块" align="center" width="320">
        <template slot-scope="scope">
          <span class="article">{{ scope.row.operModule }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作类型" align="center" width="80">
        <template slot-scope="scope">
          {{ scope.row.operTypeName }}
        </template>
      </el-table-column>
      <el-table-column label="操作描述" align="center" width="240">
        <template slot-scope="scope">
          {{ scope.row.operDesc }}
        </template>
      </el-table-column>
      <el-table-column label="操作方法" align="center" width="400">
        <template slot-scope="scope">
          {{ scope.row.operMethod }}
        </template>
      </el-table-column>
      <el-table-column label="接口地址" align="center" width="400">
        <template slot-scope="scope">
          {{ scope.row.operUrl }}
        </template>
      </el-table-column>
      <el-table-column label="请求方式" align="center" width="80">
        <template slot-scope="scope">
          {{ scope.row.requestMethod }}
        </template>
      </el-table-column>
      <el-table-column label="操作人" align="center" width="150">
        <template slot-scope="scope">
          {{ scope.row.operNickName }}
        </template>
      </el-table-column>
      <el-table-column label="IP 地址" align="center" width="140">
        <template slot-scope="scope">
          <span class="ip location">{{ scope.row.operIp }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地理位置" align="center" width="140">
        <template slot-scope="scope">
          <span class="ip location">{{ scope.row.operLocation }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作时间" align="center" width="175">
        <template slot-scope="scope">
          <i class="el-icon-time"/>
          {{ scope.row.operTime }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="small" @click="clickView(scope.$index, scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <br>
    <el-pagination background layout="prev, pager, next" :total="pageInfo.total" :page-size="pageInfo.pageSize" @next-click="nextClick" @prev-click="prevClick"
                   @current-change="currentChange"></el-pagination>

    <el-dialog title="日志详情" :visible.sync="operLogDialogVisible" width="40%">
      <el-form :model="operLogInfo" label-width="80px" size="small">
        <el-row>
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <el-form-item label="操作模块" :label-width="formLabelWidth">
              <el-input v-model="operLogInfo.operModule" readonly/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <el-form-item label="操作描述" :label-width="formLabelWidth">
              <el-input v-model="operLogInfo.operDesc" readonly/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="24" :md="18" :lg="18" :xl="18">
            <el-form-item label="操作方法" :label-width="formLabelWidth">
              <el-input v-model="operLogInfo.operMethod" readonly/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6">
            <el-form-item label="操作类型" :label-width="formLabelWidth">
              <el-input v-model="operLogInfo.operTypeName" readonly/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="24" :md="18" :lg="18" :xl="18">
            <el-form-item label="接口地址" :label-width="formLabelWidth">
              <el-input v-model="operLogInfo.operUrl" readonly/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6">
            <el-form-item label="请求方式" :label-width="formLabelWidth">
              <el-input v-model="operLogInfo.requestMethod" readonly/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
            <el-form-item label="请求参数" :label-width="formLabelWidth">
              <el-input type="textarea" v-model="operLogInfo.operReqParam" :autosize="{ minRows: 6, maxRows: 30}" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
            <el-form-item label="返回参数" :label-width="formLabelWidth">
              <el-input type="textarea" v-model="operLogInfo.operResParam" :autosize="{ minRows: 6, maxRows: 10}" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
            <el-form-item label="操作人" :label-width="formLabelWidth">
              <el-input v-model="operLogInfo.operNickName" readonly/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
            <el-form-item label="IP地址" :label-width="formLabelWidth">
              <el-input v-model="operLogInfo.operIp" readonly/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
            <el-form-item label="地理位置" :label-width="formLabelWidth">
              <el-input v-model="operLogInfo.operLocation" readonly/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { operLogsApi } from '@/api/log'

export default {
  name: 'browseRecord',
  data() {
    return {
      pageInfo: {
        pageNum: 1, // 当前页
        pageSize: 20, // 每页显示的数量
        total: 0, // 总数量
        list: [], // 数据
        prePage: 0, // 上一页
        nextPage: 0 // 下一页
      },
      operLogDialogVisible: false,
      operLogInfo: {
        operModule: '',
        operTypeName: '',
        operMethod: '',
        operDesc: '',
        operUrl: '',
        requestMethod: '',
        operReqParam: '',
        operResParam: '',
        operNickName: '',
        operIp: '',
        operLocation: '',
        operTime: ''
      },
      formLabelWidth: '120px'

    }
  },
  created() {
    this.init()
  },
  methods: {
    /**
     * 初始化方法
     */
    init() {
      this.listOperLog()
    },
    listOperLog() {
      const params = {
        pageNum: this.pageInfo.pageNum,
        pageSize: this.pageInfo.pageSize
      }
      operLogsApi(params).then(res => {
        if (res.data.resultCode == 1) {
          this.pageInfo = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    nextClick() {
      this.pageInfo.pageNum = this.pageInfo.nextPage
      this.listOperLog()
    },
    prevClick() {
      this.pageInfo.pageNum = this.pageInfo.prePage
      this.listOperLog()
    },
    currentChange(pageNum) {
      this.pageInfo.pageNum = pageNum
      this.listOperLog()
    }, clickView(index, row) {
      this.operLogDialogVisible = true
      this.operLogInfo = row
      this.operLogInfo.operReqParam = this.formatterJson(row.operReqParam, function() {

      })
      this.operLogInfo.operResParam = this.formatterJson(row.operResParam, function() {

      })
    }, formatterJson(jsonObj, callback) {
      // 正则表达式匹配规则变量
      var reg = null
      // 转换后的字符串变量
      var formatted = ''
      // 换行缩进位数
      var pad = 0
      // 一个tab对应空格位数
      var PADDING = '    '
      // json对象转换为字符串变量
      var jsonString = this.transitionJsonToString(jsonObj, callback)
      if (!jsonString) {
        return jsonString
      }
      // 存储需要特殊处理的字符串段
      var _index = []
      // 存储需要特殊处理的“再数组中的开始位置变量索引
      var _indexStart = null
      // 存储需要特殊处理的“再数组中的结束位置变量索引
      var _indexEnd = null
      // 将jsonString字符串内容通过\r\n符分割成数组
      var jsonArray = []
      // 正则匹配到{,}符号则在两边添加回车换行
      jsonString = jsonString.replace(/([\{\}])/g, '\r\n$1\r\n')
      // 正则匹配到[,]符号则在两边添加回车换行
      jsonString = jsonString.replace(/([\[\]])/g, '\r\n$1\r\n')
      // 正则匹配到,符号则在两边添加回车换行
      jsonString = jsonString.replace(/(\,)/g, '$1\r\n')
      // 正则匹配到要超过一行的换行需要改为一行
      jsonString = jsonString.replace(/(\r\n\r\n)/g, '\r\n')
      // 正则匹配到单独处于一行的,符号时需要去掉换行，将,置于同行
      jsonString = jsonString.replace(/\r\n\,/g, ',')
      // 特殊处理双引号中的内容
      jsonArray = jsonString.split('\r\n')
      jsonArray.forEach(function(node, index) {
        // 获取当前字符串段中"的数量
        var num = node.match(/\"/g) ? node.match(/\"/g).length : 0
        // 判断num是否为奇数来确定是否需要特殊处理
        if (num % 2 && !_indexStart) {
          _indexStart = index
        }
        if (num % 2 && _indexStart && _indexStart != index) {
          _indexEnd = index
        }
        // 将需要特殊处理的字符串段的其实位置和结束位置信息存入，并对应重置开始时和结束变量
        if (_indexStart && _indexEnd) {
          _index.push({
            start: _indexStart,
            end: _indexEnd
          })
          _indexStart = null
          _indexEnd = null
        }
      })
      // 开始处理双引号中的内容，将多余的"去除
      _index.reverse().forEach(function(item, index) {
        var newArray = jsonArray.slice(item.start, item.end + 1)
        jsonArray.splice(item.start, item.end + 1 - item.start, newArray.join(''))
      })
      // 奖处理后的数组通过\r\n连接符重组为字符串
      jsonString = jsonArray.join('\r\n')
      // 将匹配到:后为回车换行加大括号替换为冒号加大括号
      jsonString = jsonString.replace(/\:\r\n\{/g, ':{')
      // 将匹配到:后为回车换行加中括号替换为冒号加中括号
      jsonString = jsonString.replace(/\:\r\n\[/g, ':[')
      // 将上述转换后的字符串再次以\r\n分割成数组
      jsonArray = jsonString.split('\r\n')
      // 将转换完成的字符串根据PADDING值来组合成最终的形态
      jsonArray.forEach(function(item, index) {
        console.log(item)
        var i = 0
        // 表示缩进的位数，以tab作为计数单位
        var indent = 0
        // 表示缩进的位数，以空格作为计数单位
        var padding = ''
        if (item.match(/\{$/) || item.match(/\[$/)) {
          // 匹配到以{和[结尾的时候indent加1
          indent += 1
        } else if (item.match(/\}$/) || item.match(/\]$/) || item.match(/\},$/) || item.match(/\],$/)) {
          // 匹配到以}和]结尾的时候indent减1
          if (pad !== 0) {
            pad -= 1
          }
        } else {
          indent = 0
        }
        for (i = 0; i < pad; i++) {
          padding += PADDING
        }
        formatted += padding + item + '\r\n'
        pad += indent
      })
      // 返回的数据需要去除两边的空格
      return formatted.trim()
    }, transitionJsonToString(jsonObj, callback) {
      // 转换后的jsonObj受体对象
      var _jsonObj = null
      // 判断传入的jsonObj对象是不是字符串，如果是字符串需要先转换为对象，再转换为字符串，这样做是为了保证转换后的字符串为双引号
      if (Object.prototype.toString.call(jsonObj) !== '[object String]') {
        try {
          _jsonObj = JSON.stringify(jsonObj)
        } catch (error) {
          // 转换失败错误信息
          console.error('您传递的json数据格式有误，请核对...')
          console.error(error)
          callback(error)
        }
      } else {
        try {
          jsonObj = jsonObj.replace(/(\')/g, '\"')
          _jsonObj = JSON.stringify(JSON.parse(jsonObj))
        } catch (error) {
          // 转换失败错误信息
          console.error('您传递的json数据格式有误，请核对...')
          console.error(error)
          callback(error)
        }
      }
      return _jsonObj
    }
  }
}
</script>

<style scoped>
.block {
  padding: 10px;
}

.location {
  font-weight: bold;
  color: #409EFF;
}


</style>

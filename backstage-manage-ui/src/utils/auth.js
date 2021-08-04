const TokenKey = 'user_token'
import cookie from 'vue-cookies'

export function getToken() {

  const token = cookie.get(TokenKey)
  if (token == null || token == '' || token == undefined || token == 'undefined') {
    return null
  }
  return token
}

export function setToken(token) {
  cookie.set(TokenKey, token, -1)
  return
}

export function removeToken() {
  global.antRouter = ''
  localStorage.removeItem('router')
  cookie.remove(TokenKey)
  // return localStorage.removeItem(TokenKey)
  return
}

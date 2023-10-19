// 封装网络请求
import axios from 'axios'

// 设置公共的请求头配置
axios.defaults.headers.common['Content-Type'] = 'application/json;charSet=UTF-8'
axios.defaults.headers.common['Access-Control-Origin'] = '*'

axios.defaults.timeout = 7000 // 设置请求超时时间


// 封装get、post、delete、put请求并导出

/**
 * get 方法封装
 * @param { string } url 请求的url地址
 * @param { object } params 请求的参数
 * @returns {Promise}
 */
export function get(url, params = {}) {
  return new Promise((resolve, reject) => {
    axios
      .get(url, {
        params: params,
      })
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}

/**
 * post 方法封装
 * @param {string} url 请求的url地址
 * @param {object} params 请求的参数
 * @returns {Promise}
 */
export function post(url, params) {
  return new Promise((resolve, reject) => {
    axios
      .post(url, params)
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}

/**
 * put 方法封装
 * @param {string} url
 * @param {*} params
 * @returns {Promise}
 */
export function put(url, params) {
  return new Promise((resolve, reject) => {
    axios
      .put(url, params)
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}

export function del(url, params) {
  return new Promise((resolve, reject) => {
    axios
      .delete(url, params)
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}

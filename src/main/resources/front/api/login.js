function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
      data
    })
}

function sendMsgApi(data) {
    return $axios({
        'url': '/user/sendMsg',
        'method': 'post',
        data
    })
}

function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}


function registerApi(data) {
  return $axios({
      'url': '/user/register',
      'method': 'post',
      data
  })
}

function checkAccountApi(data) {
  return $axios({
      'url': '/user/checkAccount',
      'method': 'post',
      data
  })
}

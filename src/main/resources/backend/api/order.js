// 查询列表页接口
const getOrderDetailPage = (params) => {
  return $axios({
    url: '/order/page',
    method: 'get',
    params
  })
}

// 查看接口
const queryOrderDetailById = (id) => {
  return $axios({
    url: `/orderDetail/${id}`,
    method: 'get'
  })
}

// 取消，派送，完成接口
const editOrderDetail = (params) => {
  return $axios({
    url: '/order',
    method: 'put',
    data: { ...params }
  })
}

// 取消，派送，完成接口
const analysisOrder = (params) => {
  return $axios({
    url: '/order/analysisOrder',
    method: 'get',
    data: { ...params }
  })
}

// 基于用户分析订单数据
const analysisOrderByCustomerApi = (params) => {
  return $axios({
    url: '/order/analysisOrderByCustomer',
    method: 'post',
    data: { ...params }
  })
}

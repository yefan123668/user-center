export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        name: 'login',
        path: '/user/login',
        component: './user/Login',
      },
      {
        name: 'register',
        path: '/user/register',
        component: './user/Register',
      },
      {
        component: './404',
      },
    ],
  },
  {
    path: '/welcome',
    name: 'welcome',
    icon: 'smile',
    component: './Welcome',
  },
  {
    path: '/admin',
    name: '管理页面',
    icon: 'crown',
    access: 'canAdmin',
    component: './Admin',
    routes: [
      {
        path: '/admin/sub-page',
        name: 'sub-page',
        icon: 'smile',
        component: './Welcome',
      },
      {path: '/admin/user-manage', name: '用户管理', icon: 'smile', component: './Admin/UserManage'},
      {
        component: './404',
      },
    ],
  },
  {
    name: '人员管理',
    icon: 'table',
    path: '/list',
    component: './Admin/UserManage',
  },
  {
    path: '/',
    redirect: '/welcome',
  },
  {
    component: './404',
  },
];

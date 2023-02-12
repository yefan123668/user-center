import Footer from '@/components/Footer';
import {register} from '@/services/ant-design-pro/api';
import {SYSTEM_LOGO, XIAOFENG_LINK} from '@/constants/index';
import {getFakeCaptcha} from '@/services/ant-design-pro/login';
import {LockOutlined, MobileOutlined, UserOutlined,} from '@ant-design/icons';
import {LoginForm, ProFormCaptcha, ProFormText,} from '@ant-design/pro-components';
import {Alert, message, Tabs} from 'antd';
import React, {useState} from 'react';
import {FormattedMessage, history, SelectLang, useIntl} from 'umi';
import styles from './index.less';

const LoginMessage: React.FC<{
  content: string;
}> = ({content}) => (
  <Alert
    style={{
      marginBottom: 24,
    }}
    message={content}
    type="error"
    showIcon
  />
);

const Register: React.FC = () => {
  const [userLoginState] = useState<API.LoginResult>({});
  const [type, setType] = useState<string>('account');

  const intl = useIntl();



  //提交表单
  const handleSubmit = async (values: API.RegisterParams) => {
    //校验两次密码是否匹配， ！==会校验类型和值
    const { userPassword, checkPassword} = values;
    if (userPassword !== checkPassword) {
      message.error('两次密码不匹配');
      return;
    }
    try {
      // 注册
      const id = await register({...values});
      if (id > 0) {
        const defaultLoginSuccessMessage = intl.formatMessage({
          id: 'pages.login.success',
          defaultMessage: '注册成功！',
        });
        message.success(defaultLoginSuccessMessage);
        /** 此方法会跳转到 redirect 参数所在的位置 */
        if (!history) return;
        const {query} = history.location;
        // history.push( '/user/login?redirect='+redirect);
        history.push({
          pathname: '/user/login',
          query
        })
        return;
      } else {
        throw Error(`register error id=${id}`)
      }

    } catch (error) {
      const defaultLoginFailureMessage = intl.formatMessage({
        id: 'pages.login.failure',
        defaultMessage: '注册失败，请重试！',
      });
      message.error(defaultLoginFailureMessage);
    }
  };
  const {status, type: loginType} = userLoginState;

  return (
    <div className={styles.container}>
      <div className={styles.lang} data-lang>
        {SelectLang && <SelectLang/>}
      </div>
      <div className={styles.content}>
        <LoginForm
          subTitle={
            <>
            <p><a href={XIAOFENG_LINK} target={"_blank"} rel="noreferrer"> 晓枫知识仓库首页 </a> </p>
            </>
          }
          submitter={{
              searchConfig: {
              submitText: "注册"
            }
          }}
          logo={<img alt="logo" src={SYSTEM_LOGO}/>}
          title="用户中心"
          initialValues={{
            autoLogin: true,
          }}

          onFinish={async (values) => {
            await handleSubmit(values as API.LoginParams);
          }}
        >
          <Tabs activeKey={type} onChange={setType}>
            <Tabs.TabPane
              key="account"
              tab={'账号密码注册'}
            />
            {/*<Tabs.TabPane*/}
            {/*  key="mobile"*/}
            {/*  tab={intl.formatMessage({*/}
            {/*    id: 'pages.login.phoneLogin.tab',*/}
            {/*    defaultMessage: '手机号注册',*/}
            {/*  })}*/}
            {/*/>*/}
          </Tabs>

          {status === 'error' && loginType === 'account' && (
            <LoginMessage
              content={intl.formatMessage({
                id: 'pages.login.accountLogin.errorMessage',
                defaultMessage: '账户或密码错误',
              })}
            />
          )}
          {type === 'account' && (
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon}/>,
                }}
                placeholder="请输入账户"
                rules={[
                  {
                    required: true,
                    message: "账户是必填项"
                  },
                  {
                    min: 4,
                    type: "string",
                    message: "账户至少四位"
                  }
                ]}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon}/>,
                }}
                placeholder="请输入密码"
                rules={[
                  {
                    required: true,
                    message: "密码是必填项",
                  },
                  {
                    min: 8,
                    type: "string",
                    message: "密码至少八位"
                  }
                ]}
              />
              <ProFormText.Password
                name="checkPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon}/>,
                }}
                placeholder="请输入确认密码"
                rules={[
                  {
                    required: true,
                    message: "密码是必填项",
                  },
                  {
                    min: 8,
                    type: "string",
                    message: "密码至少八位"
                  }
                ]}
              />
            </>
          )}

          {status === 'error' && loginType === 'mobile' && <LoginMessage content="验证码错误"/>}
          {type === 'mobile' && (
            <>
              <ProFormText
                fieldProps={{
                  size: 'large',
                  prefix: <MobileOutlined className={styles.prefixIcon}/>,
                }}
                name="mobile"
                placeholder={intl.formatMessage({
                  id: 'pages.login.phoneNumber.placeholder',
                  defaultMessage: '手机号',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.phoneNumber.required"
                        defaultMessage="请输入手机号！"
                      />
                    ),
                  },
                  {
                    pattern: /^1\d{10}$/,
                    message: (
                      <FormattedMessage
                        id="pages.login.phoneNumber.invalid"
                        defaultMessage="手机号格式错误！"
                      />
                    ),
                  },
                ]}
              />
              <ProFormCaptcha
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon}/>,
                }}
                captchaProps={{
                  size: 'large',
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.captcha.placeholder',
                  defaultMessage: '请输入验证码',
                })}
                captchaTextRender={(timing, count) => {
                  if (timing) {
                    return `${count} ${intl.formatMessage({
                      id: 'pages.getCaptchaSecondText',
                      defaultMessage: '获取验证码',
                    })}`;
                  }
                  return intl.formatMessage({
                    id: 'pages.login.phoneLogin.getVerificationCode',
                    defaultMessage: '获取验证码',
                  });
                }}
                name="captcha"
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.captcha.required"
                        defaultMessage="请输入验证码！"
                      />
                    ),
                  },
                ]}
                onGetCaptcha={async (phone) => {
                  const result = await getFakeCaptcha({
                    phone,
                  });
                  // @ts-ignore
                  if (result === false) {
                    return;
                  }
                  message.success('获取验证码成功！验证码为：1234');
                }}
              />
            </>
          )}

        </LoginForm>
      </div>
      <Footer/>
    </div>
  );
};

export default Register;

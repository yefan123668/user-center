import {GithubOutlined} from '@ant-design/icons';
import {DefaultFooter} from '@ant-design/pro-components';
import {useIntl} from 'umi';

const Footer: React.FC = () => {
  const intl = useIntl();
  const defaultMessage = intl.formatMessage({
    id: 'app.copyright.produced123',
    defaultMessage: '晓枫出品',
  });

  const currentYear = new Date().getFullYear();

  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'usercenter',
          title: '用户中心',
          href: 'http://minsf.top',
          blankTarget: true,
        },
        {
          key: 'github',
          //对象必须要有一个根标签套起来
          title: <><GithubOutlined/> 晓枫 Github </>,
          href: 'https://gitee.com/min_shangfeng/user-center-pro',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;

/* 一、用一条SQL语句查询出每门课都大于80分的学生姓名
        name   kecheng   fenshu
        张三    语文    81
        张三    数学    75
        李四    语文    76
        李四    数学     90
        王五    语文    81
        王五    数学    100
        王五    英语    90
*/
    --(1)建表：create table if not exists student(name string,kecheng string,fenshu int) row format delimited fields terminated by '/t';
    --(2)插数：insert into student values("张三","语文",81),("张三","数学",75),("李四","语文",76),("李四","数学",90),("王五","语文",81),("王五","数学",100),("王五","英语",90);
    --(3)答案：
        select name from student group by name having min(fenshu) > 80;

/*
2. 学生表 如下:
    自动编号   学号  姓名 课程编号 课程名称 分数
    1     2005001 张三   0001   数学   69
    2     2005002 李四   0001   数学   89
    3     2005001 张三   0001   数学   69
删除除了自动编号不同, 其他都相同的学生冗余信息
*/
    --(1)建表：create table if not exists student1(id int,sid int,name string,kechengid string,kecheng string,fenshu int) row format delimited fields terminated by '/t';
    --(2)插数：insert into student1 values(1,2005001,"张三","0001","数学",69),(2,2005002,"李四","0001","数学",89),(3,2005001,"张三","0001","数学",69);
    --(3)答案：
        insert overwrite table student1 select min(id),sid,name,kechengid,kecheng,fenshu from student1 group by sid,name,kechengid,kecheng,fenshu;

/* 三、一个叫team的表,里面只有一个字段name,一共有4条纪录,分别是a,b,c,d,对应四个球队,现在四个球队进行比赛,用一条sql语句显示所有可能的比赛组合.
 */
    --(1)建表：create table if not exists student3(name string);
    --(2)插数：insert into table student3 values("a"),("b"),("c"),("d");
    --(3)答案：
              select a.name, b.name from team a,team b where a.name < b.name

/* 四、面试题：怎么把这样一个
          year   month      amount
          1991   1      1.1
          1991   2      1.2
          1991   3      1.3
          1991   4      1.4
          1992   1      2.1
          1992   2      2.2
          1992   3      2.3
          1992   4      2.4
        查成这样一个结果
          year m1  m2  m3   m4
          1991 1.1 1.2 1.3 1.4
          1992 2.1 2.2 2.3 2.4
 */
    --(1)建表：create table if not exists datatime(year int,month int,amount double);
    --(2)插数：insert into table datatime values(1991,1,1.1),(1991,2,1.2),(1991,3,1.3),(1991,4,1.4),(1992,1,2.1),(1992,2,2.2),(1992,3,2.3),(1992,4,2.4);
    --(3)答案：
              select year,sum(if(month=1,amount,0)) m1,sum(if(month=2,amount,0)) m2,sum(if(month=3,amount,0)) m3,sum(if(month=4,amount,0)) m4 from datatime group by year;

/* 五、说明：复制表(只复制结构,源表名：a新表名：b) */
    --(3)答案：create table b as select * from a where 1=2

/* 六、原表:
                    courseid coursename score
                    -------------------------------------
                    1 java    70
                    2 oracle  90
                    3 xml     40
                    4 jsp     30
                    5 servlet 80
                    -------------------------------------
                    为了便于阅读,查询此表后的结果显式如下(及格分数为60):
                    courseid coursename score mark
                    ---------------------------------------------------
                    1 java    70    pass
                    2 oracle  90    pass
                    3 xml     40    fail
                    4 jsp     30    fail
                    5 servlet 80    pass
*/
    --(3)答案：
              select courseid,coursename,score,if(score >= 60,"pass","fail") as mark from course

/* 七、表名：购物信息如下。给出所有购入商品为两种或两种以上的购物人记录
                    购物人      商品名称     数量
                    A            甲          2
                    B            乙          4
                    C            丙          1
                    A            丁          2
                    B            丙          5
                    ……
*/
    --(3)答案：
              select * from 购物信息 where 购物人 in (select 购物人 from 购物信息 group by 购物人 having count(*) >= 2);

/* 八、info 表
                date        result
                2005-05-09  win
                2005-05-09  lose
                2005-05-09  lose
                2005-05-09  lose
                2005-05-10  win
                2005-05-10  lose
                2005-05-10  lose
                如果要生成下列结果, 该如何写sql语句?
                   　　    win lose
                2005-05-09  2   2
                2005-05-10  1   2
*/
    --(1)建表：create table if not exists datare(datetime string,result string) row format delimited fields terminated by '/t';
    --(2)插数：insert oerwrite datare values("2005-05-09","win"),("2005-05-09","lose"),("2005-05-09","lose"),("2005-05-09","lose"),("2005-05-10","win"),("2005-05-10","lose"),("2005-05-10","lose");
    --(3)答案：
              select datetime,sum(if(result="win",1,0)) as win,sum(if(result="lose",1,0)) as lose from datare group by datetime

/* 九、手写HQL 第10题
        1、有三张表分别为会员表（member）销售表（sale）退货表（regoods）
        （1）会员表有字段memberid（会员id,主键）credits（积分）；
        （2）销售表有字段memberid（会员id,外键）购买金额（MNAccount）；
        （3）退货表中有字段memberid（会员id,外键）退货金额（RMNAccount）；
        2、业务说明：
        （1）销售表中的销售记录可以是会员购买,也可是非会员购买。（即销售表中的memberid可以为空）
        （2）销售表中的一个会员可以有多条购买记录
        （3）退货表中的退货记录可以是会员,也可是非会员。一个会员可以有一条或多条退货记录
        查询需求：分组查出销售表中所有会员购买金额,同时分组查出退货表中所有会员的退货金额,把会员id相同的购买金额-退款金额得到的结果更新到表会员表中对应会员的积分字段（credits）
*/
    --(3)答案：
        select t1.memberid,t2.MNAccount-t3.RMNAccount as credits from member t1
        left join
        (select memberid,sum(MNAccount) as MNAccount from sale where memberid is not null group by memberid) t2 on t1.memberid = t2.memberid
        left join
        (select memberid,sum(RMNAccount) as RMNAccount from regoods where memberid is not null group by memberid) t3 on t1.memberid = t3.memberid

/* 十一、手写HQL 第11题
                现在有三个表student（学生表）、course(课程表)、score（成绩单）,结构如下：
                create table student
                (
                  id bigint comment ‘学号’,
                  name string comment ‘姓名’,
                  age bigint comment ‘年龄’
                );
                create table course
                (
                  cid string comment ‘课程号,001/002格式’,
                  cname string comment ‘课程名’
                );
                Create table score
                (
                  Id bigint comment ‘学号’,
                  cid string comment ‘课程号’,
                  score bigint comment ‘成绩’
                ) partitioned by(event_day string)

                其中score中的id、cid,分别是student、course中对应的列请根据上面的表结构,回答下面的问题
                1）请将本地文件（/home/users/test/20190301.csv）文件,加载到分区表score的20190301分区中,并覆盖之前的数据
                2）查出平均成绩大于60分的学生的姓名、年龄、平均成绩
                3）查出没有‘001’课程成绩的学生的姓名、年龄
                4）查出有‘001’\’002’这两门课程下,成绩排名前3的学生的姓名、年龄
                5）创建新的表score_20190317,并存入score表中20190317分区的数据
                6）描述一下union和union all的区别,以及在mysql和HQL中用法的不同之处？
                7）简单描述一下lateral view语法在HQL中的应用场景,并写一个HQL实例
*/
    --(3)答案：
        1)load data local inpath '/home/users/test/20190301.csv' overwrite table score partition(event_day="20190301")
        2)select t1.name,t1.age,t2.avg_score
          from student t1
           left join
           (select id,avg(score) avg_score from score group by id)t2
           on t1.id = t2.id where t2.avg_score >= 60
        3)select t1.id,t1.name,t1.age from student9 t1
           left join
           (select id,sum(if(cid="001",1,0)) sum_score from score9 group by id) t2
           on t1.id = t2.id where t2.sum_score = 0;
        4)select cid,id,rnm from (select cid,id,row_number() over(partition by cid order by score desc) rnm from score9) t1 where rnm <= 3;
        5)create table score_20190317 as select * from score9 where event_day = "20190317";
        6) union的结果会自动进行"去重"且"排序",union all则是直接将两个结果集合并,不会去重和排序
-- 表1、视频表
--       字段	           备注	              详细描述
--       video id	       视频唯一id	        11位字符串
--       uploader	       视频上传者	        上传视频的用户名String
--       age	           视频年龄	          视频在平台上的整数天
--       category	       视频类别	          上传视频指定的视频分类
--       length	         视频长度	          整形数字标识的视频长度
--       views	         观看次数	          视频被浏览的次数
--       rate	           视频评分	          满分5分
--       Ratings	       流量	              视频的流量，整型数字
--       conments	       评论数	            一个视频的整数评论数
--       related ids	   相关视频id	        相关视频的id，最多20个
-- 表2、用户表
--       字段	           备注	              字段类型
--       uploader	       上传者用户名	      string
--       videos	        上传视频数	        int
--       friends	        朋友数量	          int

/* 问题一、 统计视频观看数Top10
        思路：使用order by按照views字段做一个全局排序即可，同时我们设置只显示前10条。
*/
    --(3)答案：
            select videoId,uploader,age,category,length,views,rate,ratings,comments from gulivideo_orc order by views desc limit 10;

/* 问题二、统计视频类别热度Top10
        思路：
        1) 即统计每个类别有多少个视频，显示出包含视频最多的前10个类别。
        2) 我们需要按照类别group by聚合，然后count组内的videoId个数即可。
        3) 因为当前表结构为：一个视频对应一个或多个类别。所以如果要group by类别，需要先将类别进行列转行(展开)，然后再进行count即可。
        4) 最后按照热度排序，显示前10条。
*/
    --(3)答案：
            select category_name,count(*) as hot from
            (select category_name from gulivideo_orc lateral view explode(category) t_gulivideo_orc as category_name) t1
            group by category_name order by hot desc limit 10;

/* 问题三、统计出视频观看数最高的20个视频的所属类别以及类别包含Top20视频的个数
          思路：
          1) 先找到观看数最高的20个视频所属条目的所有信息，降序排列
          2) 把这20条信息中的category分裂出来(列转行)
          3) 最后查询视频分类名称和该分类下有多少个Top20的视频
*/
    --(3)答案：
            select category_name as category, count(t2.videoId) as hot_with_views from (
            select videoId, category_name from
            (select * from gulivideo_orc order by views desc limit 20) t1
            lateral view explode(category) t_catetory as category_name) t2 group by category_name order by hot_with_views desc;

/* 问题四、统计视频观看数Top50所关联视频的所属类别排序
          思路：
          1)查询出观看数最多的前50个视频的所有信息(当然包含了每个视频对应的关联视频)，记为临时表t1
          2)将找到的50条视频信息的相关视频relatedId列转行，记为临时表t2
          3)将相关视频的id和gulivideo_orc表进行inner join操作
          4) 按照视频类别进行分组，统计每组视频个数，然后排行
*/
    --(3)答案：

select
    category_name as category,
    count(t5.videoId) as hot
from (
    select
        videoId,
        category_name
    from (
        select
            distinct(t2.videoId),
            t3.category
        from (
            select
                explode(relatedId) as videoId
            from (
                select
                    *
                from
                    gulivideo_orc
                order by
                    views
                desc limit
                    50) t1) t2
        inner join
            gulivideo_orc t3 on t2.videoId = t3.videoId) t4 lateral view explode(category) t_catetory as category_name) t5
group by
    category_name
order by
    hot
desc;

/* 问题五、统计每个类别中的视频热度Top10，以Music为例
            思路：
            1) 要想统计Music类别中的视频热度Top10，需要先找到Music类别，那么就需要将category展开，所以可以创建一张表用于存放categoryId展开的数据。
            2) 向category展开的表中插入数据。
            3) 统计对应类别（Music）中的视频热度。
*/
    --(3)答案：
create table gulivideo_category(
    videoId string,
    uploader string,
    age int,
    categoryId string,
    length int,
    views int,
    rate float,
    ratings int,
    comments int,
    relatedId array<string>)
row format delimited
fields terminated by "\t"
collection items terminated by "&"
stored as orc;
--向类别表中插入数据：
insert into table gulivideo_category
    select
        videoId,
        uploader,
        age,
        categoryId,
        length,
        views,
        rate,
        ratings,
        comments,
        relatedId
    from
        gulivideo_orc lateral view explode(category) catetory as categoryId;

--统计Music类别的Top10（也可以统计其他）
select
    videoId,
    views
from
    gulivideo_category
where
    categoryId = "Music"
order by
    views
desc limit

/* 问题六、统计每个类别中视频流量Top10，以Music为例
              思路：
              1) 创建视频类别展开表（categoryId列转行后的表）
              2) 按照ratings排序即可
*/
    --(3)答案：
select
    videoId,
    views,
    ratings
from
    gulivideo_category
where
    categoryId = "Music"
order by
    ratings
desc limit
    10;

/* 问题七、统计上传视频最多的用户Top10以及他们上传的观看次数在前20的视频
            思路：
            1) 先找到上传视频最多的10个用户的用户信息
            2) 通过uploader字段与gulivideo_orc表进行join，得到的信息按照views观看次数进行排序即可。
*/
    --(3)答案：
select
    t2.videoId,
    t2.views,
    t2.ratings,
    t1.videos,
    t1.friends
from (
    select
        *
    from
        gulivideo_user_orc
    order by
        videos desc
    limit
        10) t1
join
    gulivideo_orc t2
on
    t1.uploader = t2.uploader
order by
    views desc
limit
    20;

/* 问题八、统计每个类别视频观看数Top10
      思路：
      1) 先得到categoryId展开的表数据
      2) 子查询按照categoryId进行分区，然后分区内排序，并生成递增数字，该递增数字这一列起名为rank列
      3) 通过子查询产生的临时表，查询rank值小于等于10的数据行即可。
*/
    --(3)答案：
select
    t1.*
from (
    select
        videoId,
        categoryId,
        views,
        row_number() over(partition by categoryId order by views desc) rank from gulivideo_category) t1
where
    rank <= 10;
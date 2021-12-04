#!/bin/bash
# sqoop导文件到HDP集群

  up=264867533
        for((i=1; i>0; i++))
        do
            start=$(((${i} - 1) * 80000 + 1))
            end=$((${i} * 80000))
            if [ $end -ge $up ]
            then
                end=264867533
            fi


            sql="select * from dbo.HistorySimulate where \$CONDITIONS and id between ${start} and ${end}";

        sqoop-import "-Dorg.apache.sqoop.splitter.allow_text_splitter=true" --connect jdbc:mircosoft:sqlserver:192.168.1.34:1433;databasename=KJ70N_2019 --username sa --password abc@123@!@# --hbase-table HistorySimulate --column-family info  --query "${sql}"   --hbase-row-key HistorySimulateID --hbase-create-table --driver com.mircosoft.sqlserver.jdbc.SQLServerDriver --split-by id  -m 4


            echo Sqoop import from: ${start} to: ${end} success....................................
            if [ $end -eq $up ]
            then
                break
            fi


        done

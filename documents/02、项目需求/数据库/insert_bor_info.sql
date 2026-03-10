-- Insert sample data into `bor_info`
INSERT INTO `bor_info` 
(`id`, `pid`, `goods`, `attr`, `unit`, `warehouse`, `price`, `nums`, `discount`, `dsc`, `total`, `tax`, `tat`, `tpt`, `data`, `handle`) 
VALUES
('BI0001','BOR0001','GoodsA','Color:Red','pcs','WH1',100.0,10,0.0,0.0,1000.0,5.0,50.0,1050.0,'Urgent','10'),
('BI0002','BOR0001','GoodsB','Size:L','pcs','WH2',50.0,10,0.0,0.0,500.0,5.0,25.0,525.0,'Fragile','10'),

('BI0003','BOR0002','GoodsC','Batch:2024','pcs','WH1',200.0,10,0.0,0.0,2000.0,10.0,200.0,2200.0,'Handle with care','10'),

('BI0004','BOR0003','GoodsD','Color:Blue','pcs','WH3',50.0,10,0.0,0.0,500.0,5.0,25.0,525.0,'Returnable','10'),

('BI0005','BOR0004','GoodsE','Size:M','pcs','WH2',100.0,5,0.0,0.0,500.0,5.0,25.0,525.0,'Cold chain','5'),

('BI0006','BOR0005','GoodsF','Fragile','pcs','WH1',1000.0,10,0.0,0.0,10000.0,10.0,1000.0,11000.0,'High value','10'),

('BI0007','BOR0006','GoodsG','Adjustment','pcs','WH2',100.0,6,0.0,0.0,600.0,0.0,0.0,600.0,'Adjustment entry','6'),

('BI0008','BOR0007','GoodsH','Stationery','pcs','WH3',20.0,20,0.0,0.0,400.0,0.0,0.0,400.0,'Office supplies','20'),

('BI0009','BOR0008','GoodsI','Special packaging','pcs','WH1',500.0,2,0.0,0.0,1000.0,0.0,0.0,1000.0,'Handle carefully','2');

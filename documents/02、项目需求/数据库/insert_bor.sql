-- Insert sample data into `bor`

INSERT INTO `bor` (`id`,`source`,`frame`,`supplier`,`time`,`number`,`total`,`actual`,`people`,`arrival`,`logistics`,`file`,`data`,`more`,`examine`,`state`,`user`) VALUES
('BOR0001','PO','0','ACME Ltd','2025-01-15 09:12:00','PO-2025-0001','1500.0','1500.0','alice','2025-01-20 12:00:00','"Carrier:FastShip;Track:FS123"','"/files/bor0001.pdf"','Urgent, for Q1','','1','2','alice'),
('BOR0002','PO','1','Global Trade','2024-11-02 14:30:00','PO-2024-112','3200.50','3180.5','bob','2024-11-10 10:00:00','"Carrier:SeaLine;ETA:10d"',NULL,'Include fragile items','','1','1','bob'),
('BOR0003','WH','2','SupplyCo','2025-03-05 08:00:00','WH-2025-305','780.75','780.75',NULL,NULL,NULL,NULL,'Returnable packaging','','0','0','carol'),
('BOR0004','PO','0','FreshFarms','2025-06-20 16:45:00','PO-2025-0620','245.6','245.6','dan','2025-06-22 09:00:00','"Local pickup"','"/files/bor0004.jpg"','Cold chain','','1','2','dan'),
('BOR0005','PO','3','TechParts','2024-12-30 11:11:11','PO-2024-1230','15999.99','15800.0','erin','2025-01-05 15:30:00','"Air freight"',NULL,'High value, insure','','1','3','erin'),
('BOR0006','ADJ','2','BuildMasters','2025-02-14 10:10:10','ADJ-2025-0214','600.0','600.0','frank',NULL,NULL,NULL,'Adjustment entry','','1','0','frank'),
('BOR0007','PO','1','PaperGoods','2025-07-01 09:00:00','PO-2025-0701','420.0','420.0','gina','2025-07-02 08:30:00','"Courier: QuickPost"',NULL,'Stationery order','','0','1','gina'),
('BOR0008','PO','0','Alpha Logistics','2025-09-18 13:20:00','PO-2025-0918','999.95','999.95','harry','2025-09-25 16:00:00','"Multimodal"','"/files/bor0008.zip"','Special packaging','','1','2','harry');

('BOR0009','AO','0','ABCD','2025-10-01 10:00:00','AO-2025-1001','1200.0','1200.0','ivan','2025-10-05 12:00:00','"Courier: DHL"',NULL,'Special order','','1','2','ivan')

ALTER TABLE trips ADD COLUMN sig_cost numeric;
update trips set sig_cost = 0
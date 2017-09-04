select 

max(coalesce(s.hotel_cost_daily,0) + coalesce(s.travel_cost,0) + coalesce(s.sig_cost,0)) as tot_cost,
s.client_id
from trips s
group by client_id
order by tot_cost desc
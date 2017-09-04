select 
max(coalesce(s.hotel_cost_daily,0) + coalesce(s.travel_cost,0) + coalesce(s.sig_cost,0)) as cost_trip,
p.name
s.client_id
from trips s
inner join cities p on p.id = s.city_id
group by name, client_id
order by cost_trip desc
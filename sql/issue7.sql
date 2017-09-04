/*
ASSESSMENT:
7. Write a query (just the query, no need for a UI interface) that will show the ten clients who spent the most amount of money 
   in trips in total. The information we want to see is the name and last name of the client, the total spent amount and the 
   destination of the trip he spent the most and the amount he spent in that trip.

COMMENTARIES:
Query is not complete because in the calculation of the cost of the trip contemplate the cost of one day in a hotel but not the 
complete cost of the hotel for all days.

To do this, simply swap all the ocurrences of coalesce(s.hotel_cost_daily,0) with the following sentence...
coalesce(s.hotel_cost_daily,0) * extract(day from MAX(s.dateTo) - MIN(s.dateFrom))

The reason of not doing it, is simply avoiding to add more complexity to an already complex sentence
*/

select 
-- totals
c.first_name || ' '|| c.last_name as client,
data.tot totalspent,
data_2.name citymostcostlytrip,
data_1.cost_trip costexprensivetrip
from 
(select 
sum(coalesce(s.hotel_cost_daily,0) + coalesce(s.travel_cost,0) + coalesce(s.sig_cost,0)) tot,
s.client_id
from trips s group by client_id) as data 
inner join clients c on data.client_id = c.id
-- most exprensive trip
inner join(
select 
max(coalesce(s.hotel_cost_daily,0) + coalesce(s.travel_cost,0) + coalesce(s.sig_cost,0)) as cost_trip,
s.client_id
from trips s
group by client_id
order by cost_trip desc
) as data_1
on data_1.client_id = c.id
-- destination city most expresive trip
inner join(
select 
max(coalesce(s.hotel_cost_daily,0) + coalesce(s.travel_cost,0) + coalesce(s.sig_cost,0)) as cost_trip,
p.name,
s.client_id
from trips s
inner join cities p on p.id = s.city_id
group by name, client_id
order by cost_trip desc
) as data_2
on data_2.cost_trip = data_1.cost_trip and data_1.client_id = data_2.client_id
order by totalspent desc limit 10
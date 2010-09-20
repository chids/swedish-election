<pre>
RESTful HTML XML/JSON API to browse Swedish election results



GAE

The source is packaged for Google App Engine.
To deploy in your own GAE it ought to be enough to pull down
the source and adjust the application name inside the file:
<i>war/WEB-INF/application.xml</i>



DEMO

It's running over at: http://swedish-election-api.appspot.com and it's dead
slow the first time slow since I don't persist data yet...

- List available elections:
<a href="http://swedish-election-api.appspot.com">http://swedish-election-api.appspot.com</a>
- List 2010 results:
<a href="http://swedish-election-api.appspot.com/2010/">http://swedish-election-api.appspot.com/2010/</a>
- List 2010 results by name:
<a href="http://swedish-election-api.appspot.com/2010/sort/name/asc">http://swedish-election-api.appspot.com/2010/sort/name/asc</a>
- List 2010 results by percent:
<a href="http://swedish-election-api.appspot.com/2010/sort/percent/asc">http://swedish-election-api.appspot.com/2010/sort/percent/asc</a>
- List 2010 results grouped by block:
<a href="http://swedish-election-api.appspot.com/2010/group?block=Bl%C3%A5tt:M,C,FP,KD&block=R%C3%B6tt:S,MP,V&block=Mis%C3%A4r:SD">http://swedish-election-api.appspot.com/2010/group?block=Bl%C3%A5tt:M,C,FP,KD&block=R%C3%B6tt:S,MP,V&block=Mis%C3%A4r:SD</a>



TODO

http://github.com/chids/swedish-election-api/issues/



IRONY

My plan was to attend http://codemocracy.se/ but I had other plans and other things got in the way.
The energy and time appeared on the 18th of September, the day before the 2010 election in Sweden.
At one point I actually thought I'd be able to ship before the counting of votes started. But as
always things, people and tech got in the way. So that didn't happen which kinda sucks. But for
the 2014 election, I'll be ready! ;-)



LICENSE


<a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br /><span xmlns:dc="http://purl.org/dc/elements/1.1/" property="dc:title">Swedish Election API</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="http://github.com/chids/swedish-election-api" property="cc:attributionName" rel="cc:attributionURL">Mårten Gustafson</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported License</a>.
Permissions beyond the scope of this license may be available at <a xmlns:cc="http://creativecommons.org/ns#" href="http://marten.gustafson.pp.se/" rel="cc:morePermissions">http://marten.gustafson.pp.se/</a
</pre>
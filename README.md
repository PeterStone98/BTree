# BTree
Taking one wikipedia artcle link as an input, this prgram then goes though all the hyperlinks on the page and then goes to all of those articles and repaets this process until it has a network of 500 wikipedia articles. Along the way the program is also collecting the most common words and phrases from each afticle. Using a self balancing BTree to ensure that no side is weighted more than the other, each article is added one by one.

Then using a GUI the user is able to select from two of the articles in the network and find the path connecting the two artciles by hyperlinks with the artciles with the hghest similarites. I make use of dijkstra's algorithm to find the path of most simlialrity to connect the two artciles.

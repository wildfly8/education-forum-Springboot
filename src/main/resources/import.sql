insert into t_category values (1, 'SAT English, SAT Math, ACT English, ACT Math & Science, etc', 'SAT & ACT Standard Tests');
insert into t_category values (2, 'AP Calculus AB & BC, Mathcounts, AMC 8, 10 & 12, IMO, etc', 'High School Calculus & Math Olympiad');
insert into t_category values (3, 'AP Physics 1 & 2, SAT Physics, Fnet=ma exam', 'High School Physics & Physics Olympiad');
insert into t_category values (4, 'AP Chemistry, SAT Chemistry & General Chemistry', 'High School Chemistry');
insert into t_category values (5, 'AP Statistics, Gambling Theory, Stocks & Options Trading Application, etc', 'High School Statistics');
insert into t_category values (6, 'AP Computer Science, Java, Web Servers, Introduction of Cloud Computing & Big Data', 'High School Computer Science');
insert into t_category values (7, 'AP Micro & Macro Economics, Accounting, Investment Application, etc', 'High School Economics');
insert into t_category values (8, 'AP Psychology, Religions, Philosophy, etc', 'High School Psychology');
insert into t_category values (9, 'JavaEE, JavaScript, Python, Databases, Frameworks, Full-Stack Web Development, etc', 'IT Professionals');
insert into t_category values (10, 'Real Analysis, Complex Analysis, Abstract Algebra, Topology, Number Theory, etc', 'College-Level Math');
insert into t_category values (11, 'Classical  Mechanics, Statistical Mechanics, Quantum Mechanics, General Relativity, etc', 'College-Level Physics');

insert into t_user values (1, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'colorblue2017@gmail.com', 1, '', CURRENT_TIMESTAMP, '12345678Aa', 'Welcome to Global Springs of Life!', 'alice2017');

insert into t_role values (1, 'ROLE_ADMIN');
insert into t_role values (2, 'ROLE_USER');

insert into t_user_t_role values (1, 1);
package za.co.jcarklin.popularmovies.api;

import org.junit.Test;

import java.util.List;

import za.co.jcarklin.popularmovies.model.data.MovieListing;

import static org.junit.Assert.assertEquals;

public class JsonUtilsTest {

    String movieJson = "{\n" +
            "  \"page\": 1,\n" +
            "  \"total_results\": 19825,\n" +
            "  \"total_pages\": 992,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"vote_count\": 7094,\n" +
            "      \"id\": 299536,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 8.3,\n" +
            "      \"title\": \"Avengers: Infinity War\",\n" +
            "      \"popularity\": 259.215,\n" +
            "      \"poster_path\": \"/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Avengers: Infinity War\",\n" +
            "      \"genre_ids\": [\n" +
            "        12,\n" +
            "        878,\n" +
            "        14,\n" +
            "        28\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.\",\n" +
            "      \"release_date\": \"2018-04-25\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 4070,\n" +
            "      \"id\": 383498,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.5,\n" +
            "      \"title\": \"Deadpool 2\",\n" +
            "      \"popularity\": 212.384,\n" +
            "      \"poster_path\": \"/to0spRl1CMDvyUbOnbb4fTk3VAd.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Deadpool 2\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        35,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/3P52oz9HPQWxcwHOwxtyrVV1LKi.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Wisecracking mercenary Deadpool battles the evil and powerful Cable and other bad guys to save a boy's life.\",\n" +
            "      \"release_date\": \"2018-05-15\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 431,\n" +
            "      \"id\": 345940,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.2,\n" +
            "      \"title\": \"The Meg\",\n" +
            "      \"popularity\": 164.309,\n" +
            "      \"poster_path\": \"/xqECHNvzbDL5I3iiOVUkVPJMSbc.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"The Meg\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        878,\n" +
            "        53,\n" +
            "        27\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/ibKeXahq4JD63z6uWQphqoJLvNw.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"A deep sea submersible pilot revisits his past fears in the Mariana Trench, and accidentally unleashes the seventy foot ancestor of the Great White Shark believed to be extinct.\",\n" +
            "      \"release_date\": \"2018-08-09\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 2777,\n" +
            "      \"id\": 351286,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.6,\n" +
            "      \"title\": \"Jurassic World: Fallen Kingdom\",\n" +
            "      \"popularity\": 74.468,\n" +
            "      \"poster_path\": \"/c9XxwwhPHdaImA2f1WEfEsbhaFB.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Jurassic World: Fallen Kingdom\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/3s9O5af2xWKWR5JzP2iJZpZeQQg.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Several years after the demise of Jurassic World, a volcanic eruption threatens the remaining dinosaurs on the island of Isla Nublar. Claire Dearing, the former park manager and founder of the Dinosaur Protection Group, recruits Owen Grady to help prevent the extinction of the dinosaurs once again.\",\n" +
            "      \"release_date\": \"2018-06-06\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 0,\n" +
            "      \"id\": 505954,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 0,\n" +
            "      \"title\": \"T-34\",\n" +
            "      \"popularity\": 74.142,\n" +
            "      \"poster_path\": \"/wNJF8R5QE6nBT7DQoKk8t6YD1MM.jpg\",\n" +
            "      \"original_language\": \"ru\",\n" +
            "      \"original_title\": \"Т-34\",\n" +
            "      \"genre_ids\": [\n" +
            "        10752,\n" +
            "        18,\n" +
            "        12\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/vDx8XxfYjkn573bCFGF6SkdM33Q.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"1941. WWii. the second lieutenant Nikolai ivushkin, commander of a t-34, engages in an unequal battle against the tank ace Klaus Jager in a battle near moscow. His mission is more of a suicide - to destroy a dozen german tanks, all by himself. that said, luck does favour the bold. He wins the battle, barely survives, but loses his tank and lands himself in captivity for three long years... there was little to no chance for ivushkin and Jager to meet again, but the war knows how to throw a curve ball.in the spring of 1944, the Wehrmacht commands Jager to take charge of the ohrdruf  re range and turn it into a training center for elite german armored forces, using the latest t-34 as a running target. this is how Jager and ivushkin cross paths again. Jager o ers ivushkin to become the commander of a legendary tank and pick his crew from fellow camp prisoners. Nothing goes according to plan, though, when ivushkin uses exercises for a daring and carefully planned escape.\",\n" +
            "      \"release_date\": \"2018-12-27\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 7564,\n" +
            "      \"id\": 284054,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.3,\n" +
            "      \"title\": \"Black Panther\",\n" +
            "      \"popularity\": 72.755,\n" +
            "      \"poster_path\": \"/uxzzxijgPIY7slzFvMotPv8wjKA.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Black Panther\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        14,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/b6ZJZHUdMEFECvGiDpJjlfUWela.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"King T'Challa returns home from America to the reclusive, technologically advanced African nation of Wakanda to serve as his country's new leader. However, T'Challa soon finds that he is challenged for the throne by factions within his own country as well as without. Using powers reserved to Wakandan kings, T'Challa assumes the Black Panther mantel to join with girlfriend Nakia, the queen-mother, his princess-kid sister, members of the Dora Milaje (the Wakandan 'special forces') and an American secret agent, to prevent Wakanda from being dragged into a world war.\",\n" +
            "      \"release_date\": \"2018-02-13\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 1603,\n" +
            "      \"id\": 363088,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7,\n" +
            "      \"title\": \"Ant-Man and the Wasp\",\n" +
            "      \"popularity\": 70.92,\n" +
            "      \"poster_path\": \"/rv1AWImgx386ULjcf62VYaW8zSt.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Ant-Man and the Wasp\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        14,\n" +
            "        35,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/6P3c80EOm7BodndGBUAJHHsHKrp.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"As Scott Lang awaits expiration of his term of house detention, Hope van Dyne and Dr. Hank Pym involve him in a scheme to rescue Mrs. van Dyne from the micro-universe into which she has fallen, while two groups of schemers converge on them with intentions of stealing Dr. Pym's inventions.\",\n" +
            "      \"release_date\": \"2018-07-04\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 1,\n" +
            "      \"id\": 523873,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 0,\n" +
            "      \"title\": \"Kung Fu League\",\n" +
            "      \"popularity\": 69.433,\n" +
            "      \"poster_path\": \"/rW0A73hjzPWVwADlCTLnjLhAFLX.jpg\",\n" +
            "      \"original_language\": \"zh\",\n" +
            "      \"original_title\": \"功夫联盟\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        35\n" +
            "      ],\n" +
            "      \"backdrop_path\": null,\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Martial arts comedy following a group of kung fu legends banding together to take on the bad guys. The legends includes VINCENT ZHAO reprising his role as ‘Wong Fei Hung’ with DENNIS TO once again portraying ‘Wing Chun’ master ‘Ip Man’, DANNY CHAN KWOK KWAN as ‘Chen Zhen’ and ANDY ON as master ‘Huo Yuan Jia’.\",\n" +
            "      \"release_date\": \"2018-10-19\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 33,\n" +
            "      \"id\": 493006,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 4.5,\n" +
            "      \"title\": \"Detective Conan: Zero the Enforcer\",\n" +
            "      \"popularity\": 66.218,\n" +
            "      \"poster_path\": \"/skJl9GXEXtKoPNtsgslS2swO3zp.jpg\",\n" +
            "      \"original_language\": \"ja\",\n" +
            "      \"original_title\": \"名探偵コナン ゼロの執行人\",\n" +
            "      \"genre_ids\": [\n" +
            "        16,\n" +
            "        80,\n" +
            "        9648,\n" +
            "        28,\n" +
            "        18\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/kEqeponciiz6TyuKWtnKSzXzbGa.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"There is a sudden explosion at Tokyo Summit's giant Edge of Ocean facility. The shadow of Tōru Amuro, who works for the National Police Agency Security Bureau as Zero, appears at the site. In addition, the \\\"triple-face\\\" character is known as Rei Furuya as a detective and Kogorō Mōri's apprentice, and he is also known as Bourbon as a Black Organization member. Kogorō is arrested as a suspect in the case of the explosion. Conan conducts an investigation to prove Kogorō's innocence, but Amuro gets in his way.\",\n" +
            "      \"release_date\": \"2018-04-13\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 185,\n" +
            "      \"id\": 500664,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.5,\n" +
            "      \"title\": \"Upgrade\",\n" +
            "      \"popularity\": 63.334,\n" +
            "      \"poster_path\": \"/adOzdWS35KAo21r9R4BuFCkLer6.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Upgrade\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        53,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/22cUd4Yg5euCxIwWzXrL4m4otkU.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"A brutal mugging leaves Grey Trace paralyzed in the hospital and his beloved wife dead. A billionaire inventor soon offers Trace a cure — an artificial intelligence implant called STEM that will enhance his body. Now able to walk, Grey finds that he also has superhuman strength and agility — skills he uses to seek revenge against the thugs who destroyed his life.\",\n" +
            "      \"release_date\": \"2018-06-01\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 716,\n" +
            "      \"id\": 466282,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 8.3,\n" +
            "      \"title\": \"To All the Boys I've Loved Before\",\n" +
            "      \"popularity\": 62.25,\n" +
            "      \"poster_path\": \"/hKHZhUbIyUAjcSrqJThFGYIR6kI.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"To All the Boys I've Loved Before\",\n" +
            "      \"genre_ids\": [\n" +
            "        35,\n" +
            "        10749\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/hBHxBOGQBTMX3bDmqKoAgniZ9hE.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Lara Jean's love life goes from imaginary to out of control when her secret letters to every boy she's ever fallen for are mysteriously mailed out.\",\n" +
            "      \"release_date\": \"2018-08-17\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 212,\n" +
            "      \"id\": 429300,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.4,\n" +
            "      \"title\": \"Adrift\",\n" +
            "      \"popularity\": 61.029,\n" +
            "      \"poster_path\": \"/5gLDeADaETvwQlQow5szlyuhLbj.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Adrift\",\n" +
            "      \"genre_ids\": [\n" +
            "        53,\n" +
            "        10749,\n" +
            "        12\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/64jAqTJvrzEwncD3ARZdqYLcqbc.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"A true story of survival, as a young couple's chance encounter leads them first to love, and then on the adventure of a lifetime as they face one of the most catastrophic hurricanes in recorded history.\",\n" +
            "      \"release_date\": \"2018-05-31\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 1512,\n" +
            "      \"id\": 454983,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.6,\n" +
            "      \"title\": \"The Kissing Booth\",\n" +
            "      \"popularity\": 60.527,\n" +
            "      \"poster_path\": \"/7Dktk2ST6aL8h9Oe5rpk903VLhx.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"The Kissing Booth\",\n" +
            "      \"genre_ids\": [\n" +
            "        10749,\n" +
            "        35\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/xEKx7zPEjN6meomZ7OhV82Mm2jm.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"When teenager Elle's first kiss leads to a forbidden romance with the hottest boy in high school, she risks her relationship with her best friend.\",\n" +
            "      \"release_date\": \"2018-05-11\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 473,\n" +
            "      \"id\": 447200,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6,\n" +
            "      \"title\": \"Skyscraper\",\n" +
            "      \"popularity\": 58.382,\n" +
            "      \"poster_path\": \"/5LYSsOPzuP13201qSzMjNxi8FxN.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Skyscraper\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        53,\n" +
            "        18\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/oMKFQmoVgB69fyXfSMu0lGlHJP2.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Framed and on the run, a former FBI agent must save his family from a blazing fire in the world's tallest building.\",\n" +
            "      \"release_date\": \"2018-07-11\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 7897,\n" +
            "      \"id\": 284053,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.5,\n" +
            "      \"title\": \"Thor: Ragnarok\",\n" +
            "      \"popularity\": 58.016,\n" +
            "      \"poster_path\": \"/rzRwTcFvttcN1ZpX2xv4j3tSdJu.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Thor: Ragnarok\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/kaIfm5ryEOwYg8mLbq8HkPuM1Fo.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Thor is on the other side of the universe and finds himself in a race against time to get back to Asgard to stop Ragnarok, the prophecy of destruction to his homeworld and the end of Asgardian civilization, at the hands of an all-powerful new threat, the ruthless Hela.\",\n" +
            "      \"release_date\": \"2017-10-25\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 14318,\n" +
            "      \"id\": 118340,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.9,\n" +
            "      \"title\": \"Guardians of the Galaxy\",\n" +
            "      \"popularity\": 56.425,\n" +
            "      \"poster_path\": \"/y31QB9kn3XSudA15tV7UWQ9XLuW.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Guardians of the Galaxy\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        878,\n" +
            "        12\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/bHarw8xrmQeqf3t8HpuMY7zoK4x.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser.\",\n" +
            "      \"release_date\": \"2014-07-30\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 3737,\n" +
            "      \"id\": 333339,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.7,\n" +
            "      \"title\": \"Ready Player One\",\n" +
            "      \"popularity\": 55.132,\n" +
            "      \"poster_path\": \"/pU1ULUq8D3iRxl1fdX2lZIzdHuI.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Ready Player One\",\n" +
            "      \"genre_ids\": [\n" +
            "        12,\n" +
            "        878,\n" +
            "        14\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/5a7lMDn3nAj2ByO0X1fg6BhUphR.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"When the creator of a popular video game system dies, a virtual contest is created to compete for his fortune.\",\n" +
            "      \"release_date\": \"2018-03-28\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 24,\n" +
            "      \"id\": 399360,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 5.4,\n" +
            "      \"title\": \"Alpha\",\n" +
            "      \"popularity\": 54.434,\n" +
            "      \"poster_path\": \"/afdZAIcAQscziqVtsEoh2PwsYTW.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Alpha\",\n" +
            "      \"genre_ids\": [\n" +
            "        53,\n" +
            "        12,\n" +
            "        18\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/nKMeTdm72LQ756Eq20uTjF1zDXu.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"After a hunting expedition goes awry, a young caveman struggles against the elements to find his way home.\",\n" +
            "      \"release_date\": \"2018-08-17\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 4,\n" +
            "      \"id\": 487258,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 2.8,\n" +
            "      \"title\": \"Marainthirunthu Paarkum Marmam Enna\",\n" +
            "      \"popularity\": 53.82,\n" +
            "      \"poster_path\": \"/2zfyj4gY7RHkf89ssSTly2UppMC.jpg\",\n" +
            "      \"original_language\": \"ta\",\n" +
            "      \"original_title\": \"மறைந்திருந்து பார்க்கும் மர்மம் என்ன\",\n" +
            "      \"genre_ids\": [\n" +
            "        10751,\n" +
            "        80,\n" +
            "        10749\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/lp2fC1MPUc8bCmDxJ8uXLvecyby.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"A daring young man joins a gang that is behind chain-snatching crimes in the city. What's his game plan?\",\n" +
            "      \"release_date\": \"2018-08-17\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"vote_count\": 1741,\n" +
            "      \"id\": 427641,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.1,\n" +
            "      \"title\": \"Rampage\",\n" +
            "      \"popularity\": 52.134,\n" +
            "      \"poster_path\": \"/3gIO6mCd4Q4PF1tuwcyI3sjFrtI.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Rampage\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        878,\n" +
            "        14\n" +
            "      ],\n" +
            "      \"backdrop_path\": \"/wrqUiMXttHE4UBFMhLHlN601MZh.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Primatologist Davis Okoye shares an unshakable bond with George, the extraordinarily intelligent, silverback gorilla who has been in his care since birth.  But a rogue genetic experiment gone awry mutates this gentle ape into a raging creature of enormous size.  To make matters worse, it’s soon discovered there are other similarly altered animals.  As these newly created alpha predators tear across North America, destroying everything in their path, Okoye teams with a discredited genetic engineer to secure an antidote, fighting his way through an ever-changing battlefield, not only to halt a global catastrophe but to save the fearsome creature that was once his friend.\",\n" +
            "      \"release_date\": \"2018-04-12\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";


            @Test
            public void processMovieResults_isCorrect() {
                List<MovieListing> results = JsonUtils.getInstance().processMovieListingResults(movieJson);
                assertEquals(20,results.size());
            }
}
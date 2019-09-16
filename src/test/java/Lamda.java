import chapter1.Album;
import chapter1.Artist;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Lamda {
    public static void main(String[] args) {
        int sum = addUp(Stream.of(1, 2, 3));
        System.out.println(sum);
    }


    public static int addUp(Stream<Integer> numbers) {
        return numbers.reduce(0, (acc, x) -> acc + x);
    }


    public static List<String> getNamesAndOrigins(List<Artist> artists) {
        return artists.stream()
                .flatMap(artist -> Stream.of(artist.getName(), artist.getNationality()))
                .collect(toList());
    }

    public static List<Album> getAlbumsWithAtMostThreeTracks(List<Album> albums) {
        return albums.stream()
                .filter(album -> album.getTrackList().size() <= 3)
                .collect(toList());
    }

    public static int getCountOfMembers(List<Artist> artists) {
        return artists.stream()
                .map(artist -> artist.getMembers().count())
                .reduce(0L, Long::sum)
                .intValue();
    }

    public static boolean getPredicate(List<Integer> numbers) {
        return numbers.stream().anyMatch(number -> number > 3);
    }
}

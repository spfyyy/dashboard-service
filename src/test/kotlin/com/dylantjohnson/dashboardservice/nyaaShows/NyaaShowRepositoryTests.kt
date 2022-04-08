package com.dylantjohnson.dashboardservice.nyaaShows

import com.dylantjohnson.dashboardservice.nyaashows.NyaaHtmlRepository
import com.dylantjohnson.dashboardservice.nyaashows.NyaaShow
import com.dylantjohnson.dashboardservice.nyaashows.NyaaShowRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class NyaaShowRepositoryTests {
    companion object {
        private val SHOWS = listOf(NyaaShow("show 1", "link 1"),
                NyaaShow("show 2", "link 2"))
        private const val HTML =
                """
<!DOCTYPE html>
 <html>
     <head></head>
     <body>
         <thead></thead>
         <tbody>
             <tr class="success">
				<td>
					<a href="/?c=1_2" title="Anime - English-translated">
						<img src="/static/img/icons/nyaa/1_2.png" alt="Anime - English-translated" class="category-icon">
					</a>
				</td>
				<td colspan="2">
					<a href="/view/123" title="show 1">show 1</a>
				</td>
				<td class="text-center">
					<a href="/download/123.download"><i class="fa fa-fw fa-download"></i></a>
					<a href="link 1"><i class="fa fa-fw fa-magnet"></i></a>
				</td>
				<td class="text-center">13.1 GiB</td>
				<td class="text-center" data-timestamp="1649380841">2022-04-08 01:20</td>

				<td class="text-center">53</td>
				<td class="text-center">95</td>
				<td class="text-center">46</td>
			</tr>
            <tr class="danger">
				<td>
					<a href="/?c=1_2" title="Anime - English-translated">
						<img src="/static/img/icons/nyaa/1_2.png" alt="Anime - English-translated" class="category-icon">
					</a>
				</td>
				<td colspan="2">
					<a href="/view/124" title="show 2">show 2</a>
				</td>
				<td class="text-center">
					<a href="/download/124.download"><i class="fa fa-fw fa-download"></i></a>
					<a href="link 2"><i class="fa fa-fw fa-magnet"></i></a>
				</td>
				<td class="text-center">214.0 MiB</td>
				<td class="text-center" data-timestamp="1649378874">2022-04-08 00:47</td>

				<td class="text-center">46</td>
				<td class="text-center">16</td>
				<td class="text-center">59</td>
			</tr>
         </tbody>
         <div class="pagination-page-info">Displaying results 1-75 out of 1000 results</div>
     </body>
 </html>
                """
    }

    @Mock lateinit var nyaaHtmlRepository: NyaaHtmlRepository

    private lateinit var mRepository: NyaaShowRepository

    @BeforeEach
    fun initialize() {
        mRepository = NyaaShowRepository(nyaaHtmlRepository)
    }

    @Test
    fun shouldReturnShowsFromHtml() {
        given(nyaaHtmlRepository.getHtml()).willReturn(HTML)
        assertThat(mRepository.getShows()).isEqualTo(SHOWS)
    }

    @Test
    fun shouldReturnNullFromBadHtml() {
        given(nyaaHtmlRepository.getHtml()).willReturn(null)
        assertThat(mRepository.getShows()).isEqualTo(null)
    }
}
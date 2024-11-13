package com.eldi.akubutuhbakso

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.eldi.akubutuhbakso.domain.models.UserData
import com.eldi.akubutuhbakso.domain.models.UserDataType
import com.eldi.akubutuhbakso.domain.usecase.LocationShareInteractor
import com.eldi.akubutuhbakso.presentation.map.MapViewModel
import com.eldi.akubutuhbakso.presentation.navigation.MapDestination
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ViewModelTest {
    private lateinit var viewModel: MapViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private val useCase: LocationShareInteractor = mockk()
    private val fakeLatLng = LatLng(89.5, 111.5)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val fakeMapArgs = MapDestination(
        userName = "Ikhsan",
        userRole = UserRole.Buyer,
        timeStampIdentifier = "123",
    )
    private val onlineUsers = MutableStateFlow<UserDataType>(
        UserDataType.ListOfUsers(
            persistentListOf(),
        ),
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        mockkStatic("androidx.navigation.SavedStateHandleKt")

        val initialState = mapOf(
            "name" to fakeMapArgs.userName,
            "role" to fakeMapArgs.userRole,
            "timeStampIdentifier" to fakeMapArgs.timeStampIdentifier,
        )

        savedStateHandle = SavedStateHandle(initialState)
        every { savedStateHandle.toRoute<MapDestination>() } returns fakeMapArgs

        coEvery {
            useCase.listenToAllOnlineUsers(
                fakeMapArgs.userName,
                fakeMapArgs.userRole,
            )
        } returns onlineUsers

        coEvery {
            useCase.updateCurrentLocation(
                userName = fakeMapArgs.userName,
                role = fakeMapArgs.userRole,
                coord = fakeLatLng,
                timeStampIdentifier = fakeMapArgs.timeStampIdentifier,
            )
        } returns Unit

        Dispatchers.setMain(testDispatcher)
        viewModel = MapViewModel(useCase, savedStateHandle)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun after() {
        Dispatchers.resetMain()
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @Test
    fun `update location state`() = testScope.runTest {
        val emissions = mutableListOf<LatLng?>()
        val job = launch { viewModel.userLastLocation.toList(emissions) }

        viewModel.updateUserLocation(fakeLatLng)
        val coords = emissions.map { it?.let { "${it.latitude}, ${it.longitude}" }.toString() }
        assertEquals(listOf("null", "89.5, 111.5"), coords)
        job.cancel()
    }

    @Test
    fun `initial user location is correct`() = testScope.runTest {
        val initialValue = viewModel.userLastLocation.first()
        assertEquals(null, initialValue)
    }

    @Test
    fun `initial loading is correct`() = testScope.runTest {
        val initialValue = viewModel.initialLoading.first()
        assertEquals(true, initialValue)
    }

    @Test
    fun `updated loading is correct`() = testScope.runTest {
        val emissions = mutableListOf<Boolean>()
        val job = launch { viewModel.initialLoading.toList(emissions) }

        viewModel.updateUserLocation(fakeLatLng)
        assertEquals(listOf(true, false), emissions)
        job.cancel()
    }

    @Test
    fun `update initial online user`() = testScope.runTest {
        viewModel.onlineUsers.test {
            // Initial state
            assertEquals(persistentListOf<UserData>(), awaitItem())

            val userList = persistentListOf(
                UserData(
                    "1",
                    "a",
                    UserRole.Seller,
                    fakeLatLng,
                    0,
                ),
                UserData(
                    "2",
                    "b",
                    UserRole.Seller,
                    fakeLatLng,
                    0,
                ),
            )

            // Add List of users
            onlineUsers.emit(
                UserDataType.ListOfUsers(
                    userList,
                ),
            )

            val addedList = awaitItem()
            println("LIST ADDED-> $addedList")
            assertEquals(
                userList,
                addedList,
            )

            // Delete
            onlineUsers.emit(
                UserDataType.UserDeleted("2"),
            )

            val expectedAfterDeleted = persistentListOf(
                UserData(
                    "1",
                    "a",
                    UserRole.Seller,
                    fakeLatLng,
                    0,
                ),
            )

            val deletedItem = awaitItem()
            println("ITEMS DELETED-> $deletedItem")
            assertEquals(
                expectedAfterDeleted,
                deletedItem,
            )

            // Add
            val addedUser = UserData(
                "3",
                "eldi",
                UserRole.Seller,
                fakeLatLng,
                0,
            )
            onlineUsers.emit(
                UserDataType.UserAdded(
                    addedUser,
                ),
            )

            val expectedAdded = (expectedAfterDeleted + addedUser).toPersistentList()

            val addedItems = awaitItem()
            println("ITEMS ADDED-> $addedItems")
            assertEquals(
                expectedAdded,
                addedItems,
            )

            // Update
//            val updatedUser = UserData(
//                "3",
//                "ikhsan",
//                UserRole.Seller,
//                fakeLatLng,
//                0,
//            )
//
//            // TODO: Shared flow not emitting this
//            onlineUsers.emit(
//                UserDataType.UserAdded(
//                    updatedUser,
//                ),
//            )
//
//            val expectedUpdated = (expectedAdded + updatedUser).distinctBy { it.timestampId }.toPersistentList()
//
//            val updatedItems = awaitItem()
//            println("ITEMS UPDATED-> $updatedItems")
//            assertEquals(
//                expectedUpdated,
//                updatedItems,
//            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}

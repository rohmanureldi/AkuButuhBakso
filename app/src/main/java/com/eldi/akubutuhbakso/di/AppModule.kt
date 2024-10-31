package com.eldi.akubutuhbakso.di

import com.eldi.akubutuhbakso.data.di.appDataModule
import com.eldi.akubutuhbakso.domain.di.appDomainModule
import com.eldi.akubutuhbakso.presentation.di.appPresentationModule
import com.eldi.akubutuhbakso.service.di.serviceModule
import com.eldi.akubutuhbakso.utils.di.utilsModule

val appModule = appDataModule + appPresentationModule + serviceModule + appDomainModule + utilsModule
